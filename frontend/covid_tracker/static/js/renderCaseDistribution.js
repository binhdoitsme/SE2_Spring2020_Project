async function renderCaseDistribution(chartdata, id='donutchart') {
    google.charts.load("current", { packages: ["corechart"] });
    google.charts.setOnLoadCallback(() => {
        var data = google.visualization.arrayToDataTable(chartdata);

        var options = {
            title: id === 'world' ? 'Cases distribution worldwide' : "Cases distribution in Vietnam",
            pieHole: 0.0,
            slices: {
                0: { color: '#e2431e' },
                1: { color: '#6f9654' },
                2: { color: '#D3D3D3' }
            },
            backgroundColor: {
                fill: '#202020',
            },
            legend: {
                textStyle: {
                    color: 'white'
                }
            },
            titleTextStyle: {
                color: 'white'
            }
        };

        var chart = new google.visualization.PieChart(document.getElementById(id));
        chart.draw(data, options);
    });
}