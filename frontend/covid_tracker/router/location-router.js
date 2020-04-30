const router = require('express').Router();
const fetch = require('node-fetch');
const CONSTANTS = require('../constants');
const BACKEND_PREFIX = CONSTANTS.BACKEND_PREFIX;
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');
const errorHandlingModal = {
    200: "component/success-modal",
    400: "component/server-failed-modal",
    500: "component/unauthenticated-modal"
};

router.get('/locations', (req, res) => {
    if (!cache.get("_locations")) {
        fetch(`${BACKEND_PREFIX}/pointOfInterest`)
            .then(resp => resp.json())
            .then(json => {
                cache.put("_locations", json, defaultTTL);
                res.json(json);
            });
    } else {
        res.json(cache.get("_locations"));
    }
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
        if (res.status === 200) {
            cache.clear();
        }
        res.status(resp.status).render(errorHandlingModal[resp.status]);
    });
});

module.exports = router;