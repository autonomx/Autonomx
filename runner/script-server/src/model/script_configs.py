import json
import logging
import os
import re

from auth.authorization import ANY_USER
from config.script.list_values import ConstValuesProvider, ScriptValuesProvider, EmptyValuesProvider, \
    DependantScriptValuesProvider, NoneValuesProvider
from model.model_helper import resolve_env_var, replace_auth_vars, is_empty, fill_parameter_values, SECURE_MASK
from react.properties import ObservableList, ObservableDict, observable_fields, Property
from utils import file_utils, string_utils
from utils.object_utils import merge_dicts

LOGGER = logging.getLogger('script_server.script_configs')


class ShortConfig(object):
    def __init__(self):
        self.name = None
        self.allowed_users = []


@observable_fields(
    'script_command',
    'description',
    'requires_terminal',
    'working_directory',
    'ansi_enabled',
    'output_files',
    '_included_config')
class ConfigModel:

    def __init__(self,
                 config_object,
                 path,
                 username,
                 audit_name,
                 pty_enabled_default=True,
                 ansi_enabled_default=True,
                 parameter_values=None):
        super().__init__()

        short_config = read_short(path, config_object)
        self.name = short_config.name
        self._ansi_enabled_default = ansi_enabled_default
        self._pty_enabled_default = pty_enabled_default
        self._config_folder = os.path.dirname(path)

        self._username = username
        self._audit_name = audit_name

        self.parameters = ObservableList()
        self.parameter_values = ObservableDict()

        self._original_config = config_object
        self._included_config_path = _TemplateProperty(config_object.get('include'),
                                                       parameters=self.parameters,
                                                       values=self.parameter_values)
        self._included_config_prop.bind(self._included_config_path, self._path_to_json)

        self._init_parameters(username, audit_name)

        if parameter_values is not None:
            self.set_all_param_values(parameter_values)
        else:
            for parameter in self.parameters:
                self.parameter_values[parameter.name] = parameter.default

        self._included_config_prop.subscribe(lambda old, new: self._reload(old))
        self._reload(None)

    def set_param_value(self, param_name, value):
        parameter = self.find_parameter(param_name)
        if parameter is None:
            LOGGER.warning('Parameter ' + param_name + ' does not exist in ' + self.name)
            return
        validation_error = parameter.validate_value(value, ignore_required=True)

        if validation_error is not None:
            self.parameter_values[param_name] = None
            raise InvalidValueException(param_name, validation_error, self.name)

        self.parameter_values[param_name] = value

    def set_all_param_values(self, param_values):
        for key, value in param_values.items():
            if self.find_parameter(key) is None:
                LOGGER.warning('Incoming value for unknown parameter ' + key)

        original_values = dict(self.parameter_values)
        processed = {}

        anything_changed = True
        while (len(processed) < len(self.parameters)) and anything_changed:
            anything_changed = False

            for parameter in self.parameters:
                if parameter.name in processed:
                    continue

                required_parameters = parameter.get_required_parameters()
                if required_parameters and any(r not in processed for r in required_parameters):
                    continue

                value = param_values.get(parameter.name)
                validation_error = parameter.validate_value(value)
                if validation_error:
                    self.parameter_values.set(original_values)
                    raise InvalidValueException(parameter.name, validation_error, self.name)

                self.parameter_values[parameter.name] = value
                processed[parameter.name] = parameter
                anything_changed = True

            if not anything_changed:
                remaining = [p.name for p in self.parameters if p.name not in processed]
                self.parameter_values.set(original_values)
                raise Exception('Could not resolve order for dependencies. Remaining: ' + str(remaining))

    def _init_parameters(self, username, audit_name):
        original_parameter_configs = self._original_config.get('parameters', [])
        for parameter_config in original_parameter_configs:
            parameter = ParameterModel(parameter_config, username, audit_name,
                                       lambda: self.parameters,
                                       self.parameter_values)
            self.parameters.append(parameter)

        self._validate_parameter_configs()

    def _reload(self, old_included_config):
        if self._included_config is None:
            config = self._original_config
        else:
            config = merge_dicts(self._original_config, self._included_config, ignored_keys=['parameters'])

        self.script_command = config.get('script_path')
        self.description = config.get('description')
        self.working_directory = config.get('working_directory')

        required_terminal = read_boolean('requires_terminal', config, self._pty_enabled_default)
        self.requires_terminal = required_terminal

        ansi_enabled = read_boolean('bash_formatting', config, self._ansi_enabled_default)
        self.ansi_enabled = ansi_enabled

        self.output_files = config.get('output_files', [])

        original_parameters_names = {p.get('name') for p in self._original_config.get('parameters', [])}

        if old_included_config and old_included_config.get('parameters'):
            old_included_param_names = {p.get('name') for p in old_included_config.get('parameters', [])}
            for param_name in old_included_param_names:
                if param_name in original_parameters_names:
                    continue

                parameter = self.find_parameter(param_name)
                self.parameters.remove(parameter)

        if self._included_config is not None:
            included_parameter_configs = self._included_config.get('parameters', [])
            for parameter_config in included_parameter_configs:
                parameter_name = parameter_config.get('name')
                parameter = self.find_parameter(parameter_name)
                if parameter is None:
                    parameter = ParameterModel(parameter_config, self._username, self._audit_name,
                                               lambda: self.parameters,
                                               self.parameter_values)
                    self.parameters.append(parameter)
                    continue
                else:
                    LOGGER.warning('Parameter ' + parameter_name + ' exists in original and included file. '
                                   + 'This is now allowed! Included parameter is ignored')
                    continue

    def find_parameter(self, param_name):
        for parameter in self.parameters:
            if parameter.name == param_name:
                return parameter
        return None

    def _validate_parameter_configs(self):
        parameters_dict = {}
        for parameter in self.parameters:
            parameters_dict[parameter.name] = parameter

        for parameter in self.parameters:
            required_parameters = parameter.get_required_parameters()
            if not required_parameters:
                continue

            for required_parameter in required_parameters:
                if required_parameter not in parameters_dict:
                    raise Exception('Missing parameter "' + required_parameter + '" for the script')
                parameter = parameters_dict[required_parameter]
                unsupported_type = None

                if parameter.constant:
                    unsupported_type = 'constant'
                elif parameter.secure:
                    unsupported_type = 'secure'
                elif parameter.no_value:
                    unsupported_type = 'no_value'

                if unsupported_type:
                    raise Exception(
                        'Unsupported parameter "' + required_parameter + '" of type "' + unsupported_type + '" in values.script! ')

    def _path_to_json(self, path):
        if path is None:
            return None

        path = file_utils.normalize_path(path, self._config_folder)

        if os.path.exists(path):
            try:
                file_content = file_utils.read_file(path)
                return json.loads(file_content)
            except:
                LOGGER.exception('Failed to load included file ' + path)
                return None
        else:
            LOGGER.warning('Failed to load included file, path does not exist: ' + path)
            return None


@observable_fields(
    'param',
    'no_value',
    'description',
    'required',
    'default',
    'type',
    'min',
    'max',
    'constant',
    '_values_provider',
    'values',
    'secure',
    'separator',
    'multiple_arguments')
class ParameterModel(object):
    def __init__(self, parameter_config, username, audit_name, other_params_supplier,
                 other_param_values: ObservableDict = None):
        self._username = username
        self._audit_name = audit_name
        self._parameters_supplier = other_params_supplier

        self.name = parameter_config.get('name')

        self._original_config = parameter_config

        self._raw_values = None

        self._parameter_values = other_param_values
        if other_param_values is not None:
            other_param_values.subscribe(self._param_values_observer)

        self._reload()

    def _reload(self):
        config = self._original_config

        self.param = config.get('param')
        self.no_value = read_boolean('no_value', config, False)
        self.description = config.get('description')
        self.required = read_boolean('required', config, False)
        self.min = config.get('min')
        self.max = config.get('max')
        self.secure = read_boolean('secure', config, False)
        self.separator = config.get('separator', ',')
        self.multiple_arguments = read_boolean('multiple_arguments', config, default=False)
        self.default = _resolve_default(config.get('default'), self._username, self._audit_name)
        self.type = config.get('type', 'text')

        constant = read_boolean('constant', config, False)
        if constant and not self.default:
            message = 'Constant should have default value specified'
            raise Exception('Failed to set parameter ' + self.name + ' to constant: ' + message)
        self.constant = constant

        raw_values = config.get('values')
        if self._raw_values != raw_values:
            self._raw_values = raw_values
            values_provider = self._create_values_provider(
                raw_values,
                self.type,
                self.constant)
            self._values_provider = values_provider
            self._reload_values()

    def _param_values_observer(self, key, old_value, new_value):
        values_provider = self._values_provider
        if values_provider is None:
            return

        if key not in values_provider.get_required_parameters():
            return

        self._reload_values()

    def _reload_values(self):
        values_provider = self._values_provider
        if not values_provider:
            self.values = None
            return

        values = values_provider.get_values(self._parameter_values)
        self.values = values

    def _create_values_provider(self, values_config, type, constant):
        if constant or ((type != 'list') and (type != 'multiselect')):
            return NoneValuesProvider()

        if is_empty(values_config):
            return EmptyValuesProvider()

        if isinstance(values_config, list):
            return ConstValuesProvider(values_config)

        elif 'script' in values_config:
            script = values_config['script']

            if '${' not in script:
                return ScriptValuesProvider(script)

            return DependantScriptValuesProvider(script, self._parameters_supplier)

        else:
            message = 'Unsupported "values" format for ' + self.name
            raise Exception(message)

    def get_required_parameters(self):
        if not self._values_provider:
            return []

        return self._values_provider.get_required_parameters()

    def value_to_str(self, value):
        if self.secure:
            return SECURE_MASK

        return str(value)

    def validate_value(self, value, *, ignore_required=False):
        if self.constant:
            return None

        if is_empty(value):
            if self.required and not ignore_required:
                return 'is not specified'
            return None

        value_string = self.value_to_str(value)

        if self.no_value:
            if value not in ['true', True, 'false', False]:
                return 'should be boolean, but has value ' + value_string
            return None

        if self.type == 'text':
            return None

        if self.type == 'file_upload':
            if not os.path.exists(value):
                return 'Cannot find file ' + value
            return None

        if self.type == 'int':
            if not (isinstance(value, int) or (isinstance(value, str) and string_utils.is_integer(value))):
                return 'should be integer, but has value ' + value_string

            int_value = int(value)

            if (not is_empty(self.max)) and (int_value > int(self.max)):
                return 'is greater than allowed value (' \
                       + value_string + ' > ' + str(self.max) + ')'

            if (not is_empty(self.min)) and (int_value < int(self.min)):
                return 'is lower than allowed value (' \
                       + value_string + ' < ' + str(self.min) + ')'
            return None

        allowed_values = self.values

        if self.type == 'list':
            if value not in allowed_values:
                return 'has value ' + value_string \
                       + ', but should be in [' + ','.join(allowed_values) + ']'
            return None

        if self.type == 'multiselect':
            if not isinstance(value, list):
                return 'should be a list, but was: ' + value_string + '(' + str(type(value)) + ')'
            for value_element in value:
                if value_element not in allowed_values:
                    element_str = self.value_to_str(value_element)
                    return 'has value ' + element_str \
                           + ', but should be in [' + ','.join(allowed_values) + ']'
            return None

        return None


def _read_name(file_path, json_object):
    name = json_object.get('name')
    if not name:
        filename = os.path.basename(file_path)
        name = os.path.splitext(filename)[0]

    return name


def read_short(file_path, json_object):
    config = ShortConfig()

    config.name = _read_name(file_path, json_object)
    config.allowed_users = json_object.get('allowed_users')

    hidden = read_boolean('hidden', json_object, False)
    if hidden:
        return None

    if config.allowed_users is None:
        config.allowed_users = ANY_USER
    elif (config.allowed_users == '*') or ('*' in config.allowed_users):
        config.allowed_users = ANY_USER

    return config


def read_boolean(name, json_object, default=None):
    value = json_object.get(name)
    if value is not None:
        if isinstance(value, bool):
            return value

        if isinstance(value, str):
            return value.lower() == 'true'

        raise Exception('"' + name + '" parameter should be true or false')
    else:
        return default


def _resolve_default(default, username, audit_name):
    if not default:
        return default

    if not isinstance(default, str):
        return default

    resolved_env_default = resolve_env_var(default)
    if resolved_env_default != default:
        return resolved_env_default

    return replace_auth_vars(default, username, audit_name)


class InvalidValueException(Exception):
    def __init__(self, param_name, validation_error, script_name) -> None:
        self.param_name = param_name
        self.validation_error = validation_error
        self.script_name = script_name


class _TemplateProperty:
    def __init__(self, template, parameters: ObservableList, values: ObservableDict, empty=None) -> None:
        self._value_property = Property(None)
        self._template = template
        self._values = values
        self._empty = empty
        self._parameters = parameters

        pattern = re.compile('\${([^}]+)\}')

        search_start = 0
        script_template = ''
        required_parameters = set()

        if template:
            while search_start < len(template):
                match = pattern.search(template, search_start)
                if not match:
                    script_template += template[search_start:]
                    break
                param_start = match.start()
                if param_start > search_start:
                    script_template += template[search_start:param_start]

                param_name = match.group(1)
                required_parameters.add(param_name)

                search_start = match.end() + 1

        self.required_parameters = tuple(required_parameters)

        self._reload()

        if self.required_parameters:
            values.subscribe(self._value_changed)
            parameters.subscribe(self)

    def _value_changed(self, parameter, old, new):
        if parameter in self.required_parameters:
            self._reload()

    def on_add(self, parameter, index):
        if parameter.name in self.required_parameters:
            self._reload()

    def on_remove(self, parameter):
        if parameter.name in self.required_parameters:
            self._reload()

    def _reload(self):
        values_filled = True
        for param_name in self.required_parameters:
            value = self._values.get(param_name)
            if is_empty(value):
                values_filled = False
                break

        if self._template is None:
            self.value = None
        elif values_filled:
            self.value = fill_parameter_values(self._parameters, self._template, self._values)
        else:
            self.value = self._empty

        self._value_property.set(self.value)

    def subscribe(self, observer):
        self._value_property.subscribe(observer)

    def unsubscribe(self, observer):
        self._value_property.unsubscribe(observer)

    def get(self):
        return self._value_property.get()
