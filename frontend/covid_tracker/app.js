const PORT = 4200;
const express = require('express');
const app = express();
const ejs = require('ejs');
const path = require('path');
const cookieParser = require('cookie-parser');
const fetch = require('node-fetch');
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

app.get('/articles', (req, res) => {
    fetch(`${hostname}:8080/articles`, {
        method: 'GET'
    }).then(resp => resp.json()).then(json => {
        const articles = {articles: json};
        res.render('component/articles', articles);
    });
});

app.post('/login', (req, res) => {
    let status = 200;
    fetch('http://localhost:8080/session', {
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

//hello from Quang computer//