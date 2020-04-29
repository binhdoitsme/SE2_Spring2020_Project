const router = require('express').Router();

router.get('/analytics', function (req, res) {
    res.render('layout/analytics');
}) 

module.exports = router;