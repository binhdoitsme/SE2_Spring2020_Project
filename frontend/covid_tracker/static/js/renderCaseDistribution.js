async function renderCaseDistribution(chartdata) {
    google.charts.load("current", { packages: ["corechart"] });
    google.charts.setOnLoadCallback(() => {
        var data = google.visualization.arrayToDataTable(chartdata);

        var options = {
            title: 'World Distribution',
            pieHole: 0.4,
            slices: {
                0: { color: '#e2431e' },
                1: { color: '#6f9654' },
                2: { color: '#D3D3D3' }
            }
        };

        var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
        chart.draw(data, options);
    });
}