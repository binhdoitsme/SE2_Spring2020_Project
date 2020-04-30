const PORT = 4200;
const express = require('express');
const app = express();
const ejs = require('ejs');
const path = require('path');
const cookieParser = require('cookie-parser');
const fetch = require('node-fetch');
const statsRouter = require('./router/stats-router');
const homeRouter = require('./router/home-router');
const loginRouter = require('./router/login-router');
const locationRouter = require('./router/location-router');
const CONSTANTS = require('./constants');
const adminRouter = require('./router/admin-router');
const pointsRouter = require('./router/point-router');
const analyticRouter = require('./router/analytics-router');
const latestStats = require('./router/distribution-router');
const hostname = CONSTANTS.BACKEND_PREFIX;

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, '/static/views'));
app.use(express.static('static'));
app.use(express.urlencoded({ extended: true }));
app.use(express.json());
app.use(cookieParser());



//Route
app.use(latestStats);
app.use(analyticRouter);
app.use(pointsRouter);
app.use(statsRouter);
app.use(homeRouter);
app.use(loginRouter);
app.use(locationRouter);
app.use(adminRouter);


app.listen(PORT, () => {
    console.log(`Application listening on port ${PORT}`);
});
