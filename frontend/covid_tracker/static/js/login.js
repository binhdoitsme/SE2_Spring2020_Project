function getLoginCredentials() {
    const form = document.querySelector('#login-form');
    const formData = new FormData(form);
    const username = formData.get('username');
    const password = formData.get('password');
    return {
        username: username,
        password: password
    };
}

function showWarning(status) {
    const resKey = status === 400 ? "unauthenticated" : "server_error";
    document.querySelector('#unauth-err').textContent = resources['en'][resKey];
}

async function login() {
    const credentials = getLoginCredentials();
    let status;
    return await fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(credentials)
    }).then(res => {
        if (status !== 200) return Promise.resolve(status);
        status = res.status;
        return res.json();
    }).then(json => {
        // if (json.authToken) {
        //     document.cookie = `authToken=${json.authToken}`;
        // } else {
        //     showWarning(status);
        // }
        return status;
    });
}
