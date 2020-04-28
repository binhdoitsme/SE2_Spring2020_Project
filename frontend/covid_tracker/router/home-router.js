// define the router related to home page
const router = require('express').Router();

router.get('/', (req, res) => {    
    if (req.cookies.authToken) {
        // todo: return the dashboard view
        res.clearCookie('authToken');
        res.render('layout/dashboard');
    } else {
        res.render('index');
    }
});

router.post('/', (req, res) => {
    let lang, status;
    const suppportedLangs = ['en', 'vi'];
    if (lang = req.query.lang && suppportedLangs.includes(lang)) {
        res.cookie(req.query.lang);
        status = 200;
    } else {
        status = 415;
    }
    res.status(status).end();
});

module.exports = router;