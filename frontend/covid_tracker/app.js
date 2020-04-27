const PORT = 9000;
const express = require('express');
const app = express();
const ejs = require('ejs');
const path = require('path');
const cookieParser = require('cookie-parser');
const fetch = require('node-fetch');
const statsRouter = require('./router/stats-router');
const homeRouter = require('./router/home-router');
const loginRouter = require('./router/login-router');
const hostname = "http://localhost";

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, '/static/views'));
app.use(express.static('static'));
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cookieParser());

app.use(homeRouter);
app.use(loginRouter);
app.use(statsRouter);

app.get('/articles', (req, res) => {
    fetch(`${hostname}:8088/articles`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {
        const articles = {articles: json};
        res.render('component/articles', articles);
    });
});

// app.get('/stats', (req, res) => {
//     fetch(`${hostname}:8080/stats`, {
//         method: 'GET'
//     }).then(resp => resp.json()).then(json => {
//         const aggregatedRecord = {aggregatedRecor: json};
//         res.render('component/aggregatedRecor-3-values', aggregatedRecor);
//     });
// });

// app.get('/stats', (req, res) => {
//     fetch(`${hostname}:8088/stats`, {
//         method: 'GET'
//     }).then(resp => resp.json()).then(json => {       
//         const record = {record: json};   
//         const latestTime = json[0].timestamp.toString().substring(1,10);
//         const vietnam = 'Vietnam';
//         const recordByPoi = {recordByPoi: json.filter(record => (record.timestamp.toString().substring(1,10) === latestTime)&& (record.poiName !== vietnam))};
//         const recordVN = {recordVN: json.filter(record => (record.timestamp.toString().substring(1,10) === latestTime)&& (record.poiName === vietnam))};   
//         res.render('component/stats-list-byLocation', recordByPoi);
//     });
// });



// app.get('/world', (req, res) => {
//     fetch(`${hostname}:8088/stats?groupby=world&latest=true&timeframe=date`, {
//         method: 'GET'
//     }).then(resp => resp.json()).then(json => {
//         // const worldStats = {worldStats: json}; 
//         const latestTime = json[0].timestamp;
//         // const increaseStats = {"infected": json[0].infected - json[1].infected,"death":json[0].death - json[1].death, "recovered":json[0].recovered - json[1].recovered};
//         const worldStats = {worldStats: json.filter(record => record.timestamp === latestTime)};     
//         res.render('component/aggregatedRecord-3-values', worldStats);
//     });
// });

app.post('/login', (req, res) => {
    let status = 200;
    fetch('http://localhost:8088/session', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(req.body)
    }).then(res => {
        status = res.status;
        return res.json();
    }).then(json => res.status(status).json(json));
});

app.listen(PORT, () => {
    console.log(`Application listening on port ${PORT}`);
});