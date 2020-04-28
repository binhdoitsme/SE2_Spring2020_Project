function renderRecordByPoi(id) {
    const placeholder = document.querySelector(`#${id}`);
    fetch('/stats', {
        method: 'GET'
    }).then(res => res.text())
    .then(html => document.getElementById('statsByLocation').innerHTML = html);   
}