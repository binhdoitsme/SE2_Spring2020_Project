const fetch = require('node-fetch')
const router = require('express').Router();
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');

// quynhtn
router.get('/statsVn', (req, res) => {
    if (!cache.get('_quynhtn_stats_vn')) {
        fetch(`http://localhost:8080/stats?groupby=country`, {
            method: 'GET'
        }).then(resp => resp.json()).then(json => {
            const latestTime = json[0].timestamp.toString().substring(1, 10);
            const vietnam = "Vietnam";
            const recordVN = { recordVN: json.filter(record => (record.timestamp.toString().substring(1, 10) === latestTime) && (record.poiName === vietnam)) };
            cache.put('_quynhtn_stats_vn', recordVN, defaultTTL);
            res.render('component/stats-Vietnam', recordVN);
        });
    } else {
        res.render('component/stats-Vietnam', cache.get('_quynhtn_stats_vn'));
    }
});

router.get('/stats', (req, res) => {
    if (!cache.get('_quynhtn_stats_world')) {
        fetch(`http://localhost:8080/stats`, {
            method: 'GET'
        }).then(resp => resp.json()).then(json => {
            json.sort(function (a, b) {
                return b.infected - a.infected;
            });

            const latestTime = json[0].timestamp.toString().substring(1, 10);
            const vietnam = 'Vietnam';
            const recordByPoi = { recordByPoi: json.filter(record => (record.timestamp.toString().substring(1, 10) === latestTime) && (record.continent !== vietnam)) };
            cache.put('_quynhtn_stats_world', recordByPoi, defaultTTL);
            res.render('component/stats-list-byLocation', recordByPoi);
        });
    } else {
        res.render('component/stats-list-byLocation', cache.get('_quynhtn_stats_world'));
    }
});


router.get('/world', (req, res) => {
    if (!cache.get('_quynhtn_stats_aggregate_world')) {
        fetch(`http://localhost:8080/stats?groupby=world&latest=true&timeframe=date`, {
            method: 'GET'
        }).then(resp => resp.json()).then(json => {
            const worldStats = { "infected": json[0].infected, "death": json[0].death, "recovered": json[0].recovered, "infectedIn": json[0].infected - json[1].infected, "deathIn": json[0].death - json[1].death, "recoveredIn": json[0].recovered - json[1].recovered };
            cache.put('_quynhtn_stats_aggregate_world', worldStats, defaultTTL);
            res.render('component/aggregatedRecord-3-values', { worldStats: worldStats });
        });
    } else {
        res.render('component/aggregatedRecord-3-values', { worldStats: cache.get('_quynhtn_stats_aggregate_world') });
    }
});

// tuquan
router.get('/getDataForMap', function (req, res) {
    if (!cache.get('_tuquan_map_data')) {
        fetch('http://localhost:8080/stats?groupby=country&latest=true')
            .then(res => res.json())
            .then(result => {
                cache.put('_tuquan_map_data', result, defaultTTL);
                res.json(result)
            });
    } else {
        res.json(cache.get('_tuquan_map_data'));
    }
})

router.get('/getDataForWorldChart', function (req, res) {
    if (!cache.get("_tuquan_data_world_chart")) {
        fetch('http://localhost:8080/stats?groupby=world')
            .then(res => res.json())
            .then(result => {
                cache.put("_tuquan_data_world_chart", result, defaultTTL);
                res.json(result);
            });
    } else {
        res.json(cache.get('_tuquan_data_world_chart'));
    }
})

router.get('/getDataForVnChart', function (req, res) {
    if (!cache.get("_tuquan_data_vietnam_chart")) {
        fetch('http://localhost:8080/stats?continent=Vietnam&timeframe=date')
            .then(res => res.json())
            .then(result => {
                cache.put("_tuquan_data_vietnam_chart", result, defaultTTL);
                res.json(result);
            });
    } else {
        res.json(cache.get('_tuquan_data_world_chart'));
    }
})

router.get('/toptencountries', (req, res) => {
    if (!cache.get("_binhdh_top_ten")) {
        fetch('http://localhost:8080/stats?groupby=country&timeframe=date&latest=true')
            .then(res => res.json())
            .then(result => {
                const resultArray = Array.from(result);
                const maxTime = Math.max(...resultArray.map(row => new Date(row.timestamp).getTime()));
                const topTen = resultArray
                    .filter(row => new Date(row.timestamp).getTime() === maxTime)
                    .sort((row_1, row_2) => new Date(row_2.infected) - new Date(row_1.infected))
                    .slice(0, 10);
                cache.put('_binhdh_top_ten', topTen, defaultTTL);
                res.render('component/top-countries.ejs', { countries: topTen });
            });
    } else {
        res.render('component/top-countries.ejs', { countries: cache.get('_binhdh_top_ten') });;
    }
});

router.get('/vietnameseLatest', (req, res) => {
    if (!cache.get("_binhdh_latest_vn_sum")) {
        fetch('http://localhost:8080/stats?groupby=country&timeframe=date&latest=true')
            .then(res => res.json())
            .then(result => {
                const resultArray = Array.from(result);
                const json = resultArray.filter(row => row.poiName === "Vietnam");
                if (!json[1]) {
                    json.push(json[0]);
                }
                const vietnameseStats = { "infected": json[0].infected, "death": json[0].death, "recovered": json[0].recovered, "infectedIn": json[0].infected - json[1].infected, "deathIn": json[0].death - json[1].death, "recoveredIn": json[0].recovered - json[1].recovered };
                cache.put('_binhdh_latest_vn_sum', vietnameseStats, defaultTTL);
                res.render('component/aggregatedRecord-3-values.ejs', { worldStats: vietnameseStats });
            });
    } else {
        res.render('component/aggregatedRecord-3-values.ejs', { worldStats: cache.get('_binhdh_latest_vn_sum') });
    }
})

router.get('/vietnameseLatestByProvince', (req, res) => {
    fetch('http://localhost:8080/stats?continent=Vietnam&latest=true')
        .then(res => res.json())
        .then(result => {
            const resultArray = Array.from(result);
            const maxTime = Math.max(...resultArray.map(row => new Date(row.timestamp).getTime()));
            const provinces = resultArray
                .filter(row => new Date(row.timestamp).getTime() === maxTime)
                .sort((row_1, row_2) => new Date(row_2.infected) - new Date(row_1.infected));
            res.render('component/top-countries.ejs', { countries: provinces });
        });
})

module.exports = router;