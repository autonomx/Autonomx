import copy
import unittest

from execution import executor
from execution.execution_service import ExecutionService
from model.script_config import ConfigModel
from tests import test_utils
from tests.test_utils import mock_object, create_audit_names, _MockProcessWrapper, _IdGeneratorMock

DEFAULT_USER = 'test_user'
DEFAULT_AUDIT_NAMES = create_audit_names(auth_username=DEFAULT_USER)


class ExecutionServiceTest(unittest.TestCase):
    def test_start_script(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertEqual(self.get_last_id(), execution_id)

    def test_is_running_after_start(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertTrue(execution_service.is_running(execution_id))

    def test_is_running_after_stop(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)
        process = self.get_process(execution_id)
        process.stop()

        self.assertFalse(execution_service.is_running(execution_id))

    def test_exit_code(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)
        process = self.get_process(execution_id)
        process.finish(22)

        self.assertEqual(22, execution_service.get_exit_code(execution_id))

    def test_running_services_when_2_started(self):
        execution_service = self.create_execution_service()
        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        self.assertCountEqual([id1, id2], execution_service.get_running_executions())

    def test_running_services_when_2_started_and_1_stopped(self):
        execution_service = self.create_execution_service()
        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        execution_service.stop_script(id2)

        self.assertCountEqual([id1], execution_service.get_running_executions())

    def test_active_executions_when_2_started(self):
        execution_service = self.create_execution_service()
        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        self.assertCountEqual([id1, id2], execution_service.get_active_executions(DEFAULT_USER))

    def test_active_executions_with_different_user(self):
        execution_service = self.create_execution_service()
        self._start(execution_service)
        self._start(execution_service)

        self.assertCountEqual([], execution_service.get_active_executions('another_user'))

    def test_active_executions_when_2_started_and_1_cleanup(self):
        execution_service = self.create_execution_service()
        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        self.get_process(id1).stop()
        execution_service.cleanup_execution(id1)

        self.assertCountEqual([id2], execution_service.get_active_executions(DEFAULT_USER))

    def test_active_executor(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertTrue(execution_service.is_active(execution_id))
        self.assertIsNotNone(execution_service.get_active_executor(execution_id))

    def test_active_executor_after_cleanup(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.get_process(execution_id).stop()
        execution_service.cleanup_execution(execution_id)

        self.assertFalse(execution_service.is_active(execution_id))
        self.assertIsNone(execution_service.get_active_executor(execution_id))

    def test_cleanup_fails_on_active_execution(self):
        execution_service = self.create_execution_service()
        id1 = self._start(execution_service)

        self.assertRaises(Exception, execution_service.cleanup_execution, id1)

    def test_can_access_same_user(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertTrue(execution_service.can_access(execution_id, DEFAULT_USER))

    def test_can_access_different_user(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertFalse(execution_service.can_access(execution_id, 'another_user'))

    def test_can_access_different_user_reversed(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service, user_id='another_user')

        self.assertFalse(execution_service.can_access(execution_id, DEFAULT_USER))

    def test_get_audit_name(self):
        execution_service = self.create_execution_service()
        execution_id = self._start(execution_service)

        self.assertEqual(DEFAULT_USER, execution_service.get_audit_name(execution_id))

    def test_get_user_parameter_values(self):
        parameter_values = {'x': 1, 'y': '2', 'z': 'True'}

        execution_service = self.create_execution_service()
        parameters = test_utils.create_simple_parameter_configs(list(parameter_values.keys()) + ['const'])
        parameters['const']['constant'] = True
        parameters['const']['default'] = 'abc'
        parameters['z']['no_value'] = True

        config_model = test_utils.create_config_model(
            'test_get_user_parameter_values',
            username=DEFAULT_USER,
            parameters=parameters.values())
        execution_id = self._start_with_config(execution_service, config_model, parameter_values)

        self.assertEqual(parameter_values, execution_service.get_user_parameter_values(execution_id))

    def test_get_script_parameter_values(self):
        parameter_values = {'x': 1, 'y': '2', 'z': 'True'}

        execution_service = self.create_execution_service()
        parameters = test_utils.create_simple_parameter_configs(list(parameter_values.keys()) + ['const'])
        parameters['const']['constant'] = True
        parameters['const']['default'] = 'abc'
        parameters['z']['no_value'] = True

        config_model = test_utils.create_config_model(
            'test_get_user_parameter_values',
            username=DEFAULT_USER,
            parameters=parameters.values())
        execution_id = self._start_with_config(execution_service, config_model, parameter_values)

        self.assertEqual({'x': 1, 'y': '2', 'z': True, 'const': 'abc'},
                         execution_service.get_script_parameter_values(execution_id))

    def test_start_listener(self):
        started_ids = []

        execution_service = self.create_execution_service()
        execution_service.add_start_listener(lambda id: started_ids.append(id))

        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        self.assertCountEqual([id1, id2], started_ids)

    def test_finish_listener(self):
        finished_ids = []

        execution_service = self.create_execution_service()
        execution_service.add_finish_listener(lambda id: finished_ids.append(id))

        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        self.assertCountEqual([], finished_ids)

        self.get_process(id2).stop()
        self.assertCountEqual([id2], finished_ids)

        self.get_process(id1).stop()
        self.assertCountEqual([id1, id2], finished_ids)

    def test_finish_listener_by_id(self):
        execution_service = self.create_execution_service()

        id1 = self._start(execution_service)
        id2 = self._start(execution_service)

        notifications = []

        execution_service.add_finish_listener(lambda: notifications.append('event'), id1)

        self.get_process(id2).stop()
        self.get_process(id1).stop()
        self.assertEqual(1, len(notifications))

    def _start(self, execution_service, user_id=DEFAULT_USER):
        execution_id = execution_service.start_script(
            self._create_script_config([]),
            {},
            user_id,
            DEFAULT_AUDIT_NAMES)
        return execution_id

    def _start_with_config(self, execution_service, config, parameter_values=None, user_id=DEFAULT_USER):
        if parameter_values is None:
            parameter_values = {}

        execution_id = execution_service.start_script(
            config,
            parameter_values,
            user_id,
            DEFAULT_AUDIT_NAMES)
        return execution_id

    def _create_script_config(self, parameter_configs):
        config = ConfigModel(
            {'name': 'script_x',
             'script_path': 'ls',
             'parameters': parameter_configs},
            'script_x.json', 'user1', 'localhost')
        return config

    def create_execution_service(self):
        file_download_feature = mock_object()
        file_download_feature.is_downloadable = lambda x: False

        execution_service = ExecutionService(self.id_generator)
        self.exec_services.append(execution_service)
        return execution_service

    def get_process(self, execution_id) -> _MockProcessWrapper:
        return self.processes[execution_id]

    def setUp(self):
        super().setUp()
        self.id_generator = _IdGeneratorMock()
        self.exec_services = []
        self.processes = {}

        def create_process(executor, command, working_directory):
            wrapper = _MockProcessWrapper(executor, command, working_directory)
            self.processes[self.get_last_id()] = wrapper
            return wrapper

        executor._process_creator = create_process

    def tearDown(self):
        super().tearDown()

        for service in self.exec_services:
            for id in service.get_running_executions():
                service.kill_script(id)

            active_exec_ids = copy.copy(service._active_executor_ids)
            for id in active_exec_ids:
                service.cleanup_execution(id)

    def get_last_id(self):
        return self.id_generator.generated_ids[-1]
