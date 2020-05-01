const router = require('express').Router();
const fetch = require('node-fetch');
const hostname = require('../constants').BACKEND_PREFIX;
const defaultTTL = require('../constants').DEFAULT_TTL;
const cache = require('memory-cache');
// TODO: add authentication for admin router 

//get stats table
router.post('/admin/statstable', async (req, res) => {
    if (req.cookies.authToken) {
        if (!cache.get("_admin_stats")) {
            const response = await fetch(`${hostname}/stats`);
            const statsJSON = await response.json();
            cache.put("_admin_stats", statsJSON, defaultTTL);
        }
    
        const location = req.query.location;
        const date = req.query.date;
        const result = cache.get("_admin_stats");
        const resultArray = Array.from(result);
        
        if (!location) {
            const filteredStats = resultArray.filter(row => new Date(row.timestamp).toISOString().startsWith(date));
            res.cookie('page', 'dashboard')
                .render('index', { 
                    authenticated: true, 
                    layoutName: "dashboard", 
                    stats: filteredStats,
                    location: "World",
                    username: req.query.username,
                    maxTime: cache.get('_max_time')
                });
        } else {
            // const availableLocations = location === "World" ? 
            //                             [ "Asia", "Europe", "Antarctica", "Africa", "Oceania", "North America", "South America", "unknown" ] : ["Vietnam"]
            // const filteredStats = resultArray.filter(row => availableLocations.contains(row.continent))
            let filteredStats;
            if (location === "World") {
                filteredStats = resultArray.filter(row => row.continent !== "Vietnam" && new Date(row.timestamp).toISOString().startsWith(date));
            } else {
                filteredStats = resultArray.filter(row => row.continent === "Vietnam" && new Date(row.timestamp).toISOString().startsWith(date));
            }
            res.render('component/statstable-standalone', {
                stats: filteredStats,
                location: location,
                maxTime: cache.get('_max_time')
            });
        }
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
        if (response.status === 200) {
            cache.clear();
        }

        const json = await response.json();
        
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
        if (response.status === 200) {
            cache.clear();
        }

        const json = await response.json();
        
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
        if (response.status === 200) {
            cache.clear();
        }

        const json = await response.json();
        
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
        if (response.status === 200) {
            cache.clear();
        }
        const json = await response.json();
        
        res.status(response.status).json(json);
    } catch (error) {
        res.status(response.status).end();
    }
});

module.exports = router;

