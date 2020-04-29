const express = require('express');
const router = express.Router();
const fetch = require('node-fetch');

router.use(express.json());
const ejs = require('ejs');

router.get('/statsVn', (req, res) => {
    fetch(`http://localhost:8080/stats?groupby=country`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {       
        const latestTime = json[0].timestamp.toString().substring(1,10);
        const vietnam = "Vietnam";
        const recordVN = { recordVN: json.filter(record => (record.timestamp.toString().substring(1, 10) === latestTime) && (record.poiName === vietnam)) };
        res.render('component/stats-Vietnam', recordVN);
    });
});

router.get('/stats', (req, res) => {
    fetch(`http://localhost:8080/stats`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {
        json.sort(function(a,b){ 
            return b.infected - a.infected; 
          });
        // const recordByPoi = {recordByPoi: json}; 
        // let recordByPoi = [];
        // for (var i=0; i<10; i++) {
        //     recordByPoi.push(recordByPo[i]);
        // }
        const latestTime = json[0].timestamp.toString().substring(1,10);
        const vietnam = 'Vietnam';
        const recordByPoi = {recordByPoi: json.filter(record => (record.timestamp.toString().substring(1,10) === latestTime)&& (record.poiName !== vietnam))};       
        res.render('component/stats-list-byLocation', recordByPoi);
    });
});



router.get('/world', (req, res) => {
    fetch(`http://localhost:8080/stats?groupby=world&latest=true&timeframe=date`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {
        // const worldStat = {worldStat: json}; 
        // const latestTime = json[0].timestamp;
        // const increaseStats = {"infected": json[0].infected - json[1].infected,"death":json[0].death - json[1].death, "recovered":json[0].recovered - json[1].recovered};
        // const worldLatest = {worldLatest: json.filter(record => record.timestamp === latestTime)};
        const worldStats = {"infected": json[0].infected,"death":json[0].death, "recovered":json[0].recovered, "infectedIn": json[0].infected - json[1].infected,"deathIn":json[0].death - json[1].death, "recoveredIn":json[0].recovered - json[1].recovered}; 
        res.render('component/aggregatedRecord-3-values', {worldStats : worldStats});
    });
});

module.exports = router;