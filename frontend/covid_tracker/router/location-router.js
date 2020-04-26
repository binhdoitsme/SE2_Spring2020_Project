const router = require('express').Router();
const fetch = require('node-fetch');
const CONSTANTS = require('../constants');
const BACKEND_PREFIX = CONSTANTS.BACKEND_PREFIX;
const errorHandlingModal = {
    200: "component/success-modal",
    400: "component/server-failed-modal",
    500: "component/unauthenticated-modal"
};

router.get('/locations', (req, res) => {
    fetch(`${BACKEND_PREFIX}/pointOfInterest`)
        .then(resp => resp.json())
        .then(json => res.json(json));
});

router.post('/locations', (req, res) => {
    let body = req.body;
    body.authToken = req.cookies.authToken;
    if (!body.authToken) {
        res.status(400).json({});
    }
    fetch(`${BACKEND_PREFIX}/pointOfInterest?authToken=${req.cookies.authToken}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: `[${JSON.stringify(req.body)}]`
    }).then(resp => {
        res.status(resp.status).render(errorHandlingModal[resp.status]);
    });
});

module.exports = router;