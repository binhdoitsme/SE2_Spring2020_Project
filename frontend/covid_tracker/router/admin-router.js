const express = require('express');
const router = require('express').Router();
const fetch = require('node-fetch');
const hostname = "http://localhost";
router.use(express.static('static'));
// TODO: add authentication for admin router 

//get stats table
router.get('/admin/statstable', async (req, res) => {
    const response = await fetch(`${hostname}:8080/stats?latest=true`);
    const statsJSON = await response.json();
    res.render('index', { stats: statsJSON });
    // res.render('component/admin-statstable',{stats:statsJSON});
});

//put new record => bindh will continue to implement user authentication here
router.put('/admin/records/add', async (req, res) => {
    const response = await fetch(`${hostname}:8080/stats`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(req.body)
    });
    const json = await response.json();
    console.log(json);
});

//delete record by id => bindh will continue to implement user authentication here
router.delete('/admin/records/delete/:id', async (req, res) => {
    const recordId = req.params.id;
    const response = await fetch(`${hostname}:8080/stats`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ recordId: recordId })
    });
    const json = await response.json();
    console.log(json);
});

module.exports = router;

