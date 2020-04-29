const fetch = require('node-fetch')
const router = require('express').Router();


router.get('/getDataForLocation', function (request, response) {
    fetch('http://localhost:8080/pointOfInterest')
        .then(res => res.json())
        .then(result => response.json(result));
})

module.exports = router;