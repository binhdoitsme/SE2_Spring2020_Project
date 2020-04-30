// define the router related to home page
const router = require('express').Router();
const fetch = require('node-fetch');
const hostname = require('../constants').BACKEND_PREFIX;
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');

router.get('/', async (req, res) => {
    let page = req.cookies.page !== 'undefined' ? req.cookies.page : "overview";
    const authenticated = req.cookies.username !== undefined;

    if (page === 'dashboard' && authenticated) {
        if (!cache.get('dashboard_list')) {
            const response = await fetch(`${hostname}/stats?latest=true`);
            const statsJSON = await response.json();
            cache.put('dashboard_list', statsJSON, defaultTTL);
            console.log('abc');
        }
        const stats = cache.get('dashboard_list');
        
        res.cookie('page', 'dashboard')
            .render('index', { authenticated: true, username: req.cookies.username, layoutName: "dashboard", stats: stats });
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

router.get('/articles', (req, res) => {
    fetch(`${hostname}/articles`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {
        const articles = { articles: json };
        res.render('component/articles', articles);
    });
});

module.exports = router;