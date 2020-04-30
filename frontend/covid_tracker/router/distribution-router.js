const fetch = require('node-fetch');
const router = require('express').Router();
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');

// render case dis
router.get('/getDistribute', async function (request, response) {
    const type = request.query.chart;
    let fetchdata = null;
    if (type === "world") {
        if (!cache.get("_world_distribution")) {
            const data = await fetch('http://localhost:8080/stats?groupby=world&latest=true');
            const json = await data.json();
            cache.put("_world_distribution", json, defaultTTL);
        }
        fetchdata = cache.get("_world_distribution");
    } else if (type === "vietnam") {
        if (!cache.get("_vietnam_distribution")) {
            const data = await fetch('http://localhost:8080/stats?groupby=country&timeframe=date&continent=Asia&latest=true');
            const json = await data.json();
            cache.put("_vietnam_distribution", json, defaultTTL);
        }
        fetchdata = cache.get("_vietnam_distribution");
    }
    const data = fetchdata
    let latest = data[0];
    if (type === "vietnam") {
        latest = Array.from(data).filter(row => row.poiName === "Vietnam")[0];
    }
    const specdata = new Array(['Title', 'Statistic'],
        ['Active', latest.infected - (latest.recovered + latest.death)],
        ['Recovered', latest.recovered],
        ['Death', latest.death]);
    response.render('component/case-distribution-chart', { chartdata: JSON.stringify(specdata), name: type })
})

module.exports = router;