function renderRecordVN(id) {
    const placeholder = document.querySelector(`#${id}`);
    fetch('/statsVn', {
        method: 'GET'
    }).then(res => res.text())
    .then(html => document.getElementById('VnStats').innerHTML = html);   
}