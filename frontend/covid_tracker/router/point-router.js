const fetch = require('node-fetch')
const router = require('express').Router();
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');

router.get('/getDataForLocation', function (req, res) {
    if (!cache.get("location_list")) {
    fetch('http://localhost:8080/pointOfInterest')
        .then(res => res.json())
        .then(result => {
            cache.put("location_list", result, defaultTTL);
            res.json(result);
        });
    } else {
        res.json(cache.get("location_list"));
    }
})

module.exports = router;