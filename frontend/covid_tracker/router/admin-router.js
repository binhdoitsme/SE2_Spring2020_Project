const router = require('express').Router();
const fetch = require('node-fetch');
const hostname = require('../constants').BACKEND_PREFIX;
// TODO: add authentication for admin router 

//get stats table
router.post('/admin/statstable', async (req, res) => {
    if (req.cookies.authToken) {
        const response = await fetch(`${hostname}/stats?latest=true`);
        const statsJSON = await response.json();
        res.cookie('page', 'dashboard')
            .render('index', { authenticated: true, layoutName: "dashboard", stats: statsJSON });
    } else {
        res.status(400).end();
    }
    // res.render('component/admin-statstable',{stats:statsJSON});
});

//put new record => bindh will continue to implement user authentication here
router.post('/admin/records/add', async (req, res) => {
    const jwt = req.cookies.authToken;
    const response = await fetch(`${hostname}/stats?authToken=${jwt}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify([req.body])
    });
    try {
        const json = await response.json();
        console.log(json);
        res.status(response.status).json(json);
    } catch (error) {
        res.status(response.status).end();
    }
    
});

//delete record by id => bindh will continue to implement user authentication here
router.delete('/admin/records/delete/:id', async (req, res) => {
    const recordId = req.params.id;
    const jwt = req.cookies.authToken;
    const response = await fetch(`${hostname}/stats?authToken=${jwt}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify([parseInt(recordId)])
    });
    try {
        const json = await response.json();
        console.log(json);
        res.status(response.status).json(json);
    } catch (error) {
        res.status(response.status).end();
    }
});

router.put('/admin/records/update', async (req, res) => {
    
    const jwt = req.cookies.authToken;
    const response = await fetch(`${hostname}/stats?authToken=${jwt}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify([req.body])
    });
    try {
        const json = await response.json();
        console.log(json);
        res.status(response.status).json(json);
    } catch (error) {
        res.status(response.status).end();
    }
});

router.put('/admin/records/updatebulk', async (req, res) => {
    
    const jwt = req.cookies.authToken;
    const response = await fetch(`${hostname}/stats?authToken=${jwt}`, {
        method: 'PUT'
    });
    try {
        const json = await response.json();
        console.log(json);
        res.status(response.status).json(json);
    } catch (error) {
        res.status(response.status).end();
    }
});

module.exports = router;

