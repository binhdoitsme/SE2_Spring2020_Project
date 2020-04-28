async function renderWorldChar() {
    const total = new Array(['Date', 'Infected', 'Death', "Recovered"]);
    const result = await (await fetch("http://localhost:4200/getDataForVnChart")).json();
    const lstDataVn = new Array;
    for (var obj of result) {
        if (new String(obj.poiName).toUpperCase() === "VIETNAM") {
            lstDataVn.push(obj)
        }
    }
    for (var obj of lstDataVn) {
        var pointoftable = [convertDate(obj), obj.infected, obj.death, obj.recovered]
        total.push(pointoftable)
    }
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(() => {
        var data = google.visualization.arrayToDataTable(total);

        var options = {
            title: 'VietNam Trend Overall',
            curveType: 'function',
            legend: { position: 'bottom' },
            series: {
                0: { color: '#e2431e' },
                2: { color: '#6f9654' },
                1: { color: '#D3D3D3' },
            },
            backgroundColor: {
                fill: '#202020',
            },
            hAxis: {
                textStyle: {
                    color: '#666666',
                    fontSize: 12,
                },
                gridlines: {
                    color: 'transparent'
                }
            },
            titleTextStyle: {
                color: '#dfe1e3',
                fontSize: 20,
                bold: false,
            },
            vAxis: {
                textStyle: {
                    color: '#666666',
                    fontSize: 16,
                },
                gridlines: {
                    color: 'transparent'
                }
            },
        };
        var chart = new google.visualization.AreaChart(document.getElementById('vietnam_chart'));
        chart.draw(data, options);
    });
}

function convertDate(object) {
    var date = new Date(object.timestamp);
    var getMonth = 1 + date.getMonth();
    var getDate = date.getDate();
    var getYear = date.getFullYear();
    if (getDate < 10) { 
        getDate = '0' + getDate; 
    } 
    if (getMonth < 10) { 
        getMonth = '0' + getMonth; 
    } 
    var result = getYear + '-' + getDate + '-' + getMonth;
    return result
}