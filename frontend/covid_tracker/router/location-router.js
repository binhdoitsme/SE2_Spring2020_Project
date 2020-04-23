const router = require('express').Router();
const fetch = require('node-fetch');

router.get('/locations', (req, res) => {
    fetch('http://localhost:8080/pointOfInterest')
        .then(resp => resp.json())
        .then(json => res.json(json));
});

module.exports = router;