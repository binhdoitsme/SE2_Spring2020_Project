// define router for login/logout
const router = require('express').Router();
const fetch = require('node-fetch');
const CONSTANTS = require('../constants');
const BACKEND_PREFIX = CONSTANTS.BACKEND_PREFIX;

router.post('/login', (req, res) => {
    let status = 200;
    fetch(`${BACKEND_PREFIX}/session`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(req.body)
    }).then(res => {
        status = res.status;
        return res.json();
    }).then(json => {
        res.cookie('authToken', json.authToken, {
            expires: new Date(Date.now() + 1800000)
        });
        res.cookie('username', req.body.username, {
            expires: new Date(Date.now() + 1800000)
        }).json(json)
    });
});

router.post('/logout', (req, res) => {
    if (!req.cookies.username) {
        res.end('Unauthenticated!');
    } else {
        for (let key in req.cookies)
            res.clearCookie(key);
        res.end('done!');
    }
});

module.exports = router;