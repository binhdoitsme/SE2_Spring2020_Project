// define the router related to home page
const router = require('express').Router();
const fetch = require('node-fetch');
const hostname = require('../constants').BACKEND_PREFIX;

router.get('/', async (req, res) => {
    let page = req.cookies.page ? req.cookies.page : "overview";
    const authenticated = req.cookies.username !== undefined;

    if (page === 'dashboard' && authenticated) {
        const response = await fetch(`${hostname}/stats?latest=true`);
        const statsJSON = await response.json();
        res.cookie('page', 'dashboard')
            .render('index', { authenticated: true, username: req.cookies.username, layoutName: "dashboard", stats: statsJSON });
    } else {
        if (authenticated === false && page === "dashboard") {
            page = "overview";
        }
        res.render('index', { 
            layoutName: page,
            authenticated: authenticated,
            username: req.cookies.username
        });
    }    
});

router.post('/', (req, res) => {
    if (req.query.page) {
        console.log(req.query.page);
        res.status(200).cookie('page', req.query.page).end();
    }
    let lang, status;
    const suppportedLangs = ['en', 'vi'];
    if (lang = req.query.lang && suppportedLangs.includes(lang)) {
        res.cookie('lang', req.query.lang);
        status = 200;
    } else {
        status = 415;
    }
    res.status(status).end();
});

module.exports = router;