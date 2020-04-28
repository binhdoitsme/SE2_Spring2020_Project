const fetch = require('node-fetch');
const router = require('express').Router();

// render case dis
router.get('/getDistribute', async function (request, response) {
    const type = request.query.chart;
    var fetchdata = null;
    if (type === "world") {
        fetchdata = await fetch('http://localhost:8080/stats?groupby=world&latest=true');
    } else if (type === "vietnam") {
        fetchdata = await fetch('http://localhost:8080/stats?continent=vietnam&timeframe=date');
    }
    const data = await fetchdata.json();
    const latest = data[0];
    const specdata = new Array(['Title', 'Statistic'],
        ['Active', latest.infected - (latest.recovered + latest.death)],
        ['Recovered', latest.recovered],
        ['Death', latest.death]);
    response.render('component/case-distribution-chart', { chartdata: JSON.stringify(specdata) })
})

module.exports = router;