const router = require("express").Router();

router.get('/map',(req,res) => {
    res.render('component/stats-map')
});

module.exports = router;