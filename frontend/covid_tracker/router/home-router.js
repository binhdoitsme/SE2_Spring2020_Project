// define the router related to home page
const router = require('express').Router();

router.get('/', (req, res) => {
    const page = req.cookies.page ? req.cookies.page : "overview";
    const authenticated = req.cookies.username !== undefined;
    res.render('index', { 
        layoutName: page,
        authenticated: authenticated
    });
});

router.post('/', (req, res) => {
    if (req.query.page) {
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