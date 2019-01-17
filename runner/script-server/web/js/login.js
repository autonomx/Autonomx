var NEXT_URL_KEY = 'next';
var OAUTH_RESPONSE_KEY = 'code';

var loginMethod = 'POST';
var loginUrl = 'login';

function onLoad() {
    callHttp('auth/config', null, 'GET', function (configResponse) {
        var loginContainer = document.getElementById('login-content-container');

        var config = JSON.parse(configResponse);
        if (config['type'] === 'google_oauth') {
            setupGoogleOAuth(loginContainer, config);
        } else {
            setupCredentials(loginContainer);
        }
    });
}

function setupCredentials(loginContainer) {
    var credentialsTemplate = createTemplateElement('login-credentials-template');
    loginContainer.appendChild(credentialsTemplate);

    var labels = document.getElementsByTagName("label");
    for (var i = 0; i < labels.length; i++) {
        var label = labels[i];

        //workaround for browser autofill - it's not updating label position and label overlap with value
        addClass(label, "active");
    }

    var form = $(loginContainer).find('.login-form').get(0);
    form.action = loginUrl;
    form.method = loginMethod;

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        var formData = new FormData(form);
        sendLoginRequest(formData);
    });
}

function setupGoogleOAuth(loginContainer, authConfig) {
    var credentialsTemplate = createTemplateElement('login-google_oauth-template');
    loginContainer.appendChild(credentialsTemplate);

    var oauthLoginButton = document.getElementById('login-google_oauth-button');
    oauthLoginButton.onclick = function () {
        var token = guid(32);

        var localState = {
            'token': token,
            'urlFragment': window.location.hash
        };
        localState[NEXT_URL_KEY] = getQueryParameter(NEXT_URL_KEY);

        saveState(localState);

        var arguments = {
            'redirect_uri': getUnparameterizedUrl(),
            'state': token,
            'client_id': authConfig['client_id'],
            'scope': authConfig['oauth_scope'],
            'response_type': OAUTH_RESPONSE_KEY
        };
        var query = $.param(arguments);
        window.location = authConfig['oauth_url'] + '?' + query;
    };

    processCurrentOauthState();
}

function processCurrentOauthState() {
    var oauthState = restoreState();

    var oauthResponseCode = getQueryParameter(OAUTH_RESPONSE_KEY);
    var queryStateToken = getQueryParameter('state');
    if (oauthState || oauthResponseCode) {
        if (!oauthState && oauthResponseCode) {
            console.log('oauth_state=' + oauthState);
            console.log('oauthResponseCode=' + oauthResponseCode);
            showError('Invalid client state. Please try to relogin');
            return;
        }

        var nextUrl = oauthState[NEXT_URL_KEY];
        var urlFragment = oauthState['urlFragment'];

        var previousLocation = getUnparameterizedUrl();
        if (nextUrl) {
            previousLocation += '?' + $.param({'next': nextUrl});
        }
        if (urlFragment) {
            previousLocation += urlFragment;
        }


        if (!oauthResponseCode) {
            if (getQueryParameter(NEXT_URL_KEY)) {
                return;
            }
            window.location = previousLocation;
        } else {
            window.history.pushState(null, '', previousLocation);
        }

        var localStateToken = oauthState.token;
        if (queryStateToken !== localStateToken) {
            showError('Invalid client state. Please try to relogin');
            return;
        }

        var formData = new FormData();
        formData.append(OAUTH_RESPONSE_KEY, oauthResponseCode);
        sendLoginRequest(formData);
    }
}

function sendLoginRequest(formData) {
    var request;

    var nextUrl = getQueryParameter(NEXT_URL_KEY);
    var nextUrlFragment = window.location.hash;

    if (nextUrl) {
        formData.append(NEXT_URL_KEY, nextUrl);
    }

    var onSuccess = function () {
        hideError();

        var redirect = request.getResponseHeader('Location');
        if (!redirect) {
            showError('Invalid server response. Please contact the administrator');
            return;
        }

        if (nextUrlFragment) {
            redirect += nextUrlFragment;
        }

        window.location = redirect;
    };

    var onError = function (errorCode, errorText) {
        if (contains([400, 401, 403, 500], errorCode)) {
            showError(errorText);

        } else {
            showError("Unknown error occurred. Please contact the administrator");
        }
    };

    request = callHttp(loginUrl, formData, loginMethod, onSuccess, onError);
}

function showError(text) {
    $('.login-error-label').text(text);
}

function hideError() {
    showError('');
}

var LOCAL_STATE_KEY = 'oauth_state';

function saveState(localState) {
    sessionStorage.setItem(LOCAL_STATE_KEY, JSON.stringify(localState));
}

function restoreState() {
    var state = JSON.parse(sessionStorage.getItem(LOCAL_STATE_KEY));
    sessionStorage.removeItem(LOCAL_STATE_KEY);
    return state;
}
