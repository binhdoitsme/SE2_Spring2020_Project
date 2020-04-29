async function getData(url) {
    const response = await fetch(url);
    return response.json();
}

async function renderMap(id='map') {
    //double fetch to complete map configuration
    //fet poiCode
    let stats = await getData('http://localhost:4200/getDataForMap');
    let location = await getData('http://localhost:4200/getDataForLocation');
    // convert to Data
    var data = new Object()
    for (var item of stats) {
        // var subObject = new Object();
        // subObject['infected'] = item.infected;
        // subObject['death'] = item.death;
        // subObject['recovered'] = item.recovered;
        var pointname = item.poiName;
        for (var element of location) {
            if (pointname == element.name) {
                pointname = element.code
            }
        }
        data[pointname] = item.infected;
    }

    $(function () {
        $(`#${id}`).vectorMap({
            map: 'world_mill',
            series: {
                regions: [
                    {
                        values: data,
                        scale: ['#FA8072', '#800000'],
                        normalizeFunction: 'polynomial'
                    }
                ]
            },
            onRegionTipShow: function (e, el, code) {
                const numOfInfected = data[code] ? data[code] : '0';
                el.html(el.html() + ': ' + numOfInfected);
            },
            backgroundColor: "#202020"
        });
        $('#map_spinner').remove();
    });
}
