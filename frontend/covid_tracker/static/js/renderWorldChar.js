async function renderWorldChar(id='curve_chart') {
    const total = new Array(['Date', 'Infected', 'Death', "Recovered"]);
    const result = await (await fetch("http://localhost:4200/getDataForWorldChart")).json();
    result.reverse();
    for (var obj of result) {
        var pointoftable = [obj.timestamp.substring(0, 10), obj.infected, obj.death, obj.recovered]
        total.push(pointoftable)
    }
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(() => {
        var data = google.visualization.arrayToDataTable(total);

        var options = {
            // title: 'World trend on CoViD-19 disease',
            curveType: 'function',
            legend: { position: 'none' },
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
                    color: 'white',
                    fontSize: 12,
                }
            },
            vAxis: {
                textStyle: {
                    color: 'white',
                    fontSize: 12,
                },
                // gridlines: {
                //     color: 'transparent'
                // }
            }
        };
        var chart = new google.visualization.AreaChart(document.getElementById(id));
        chart.draw(data, options);
    });
}

