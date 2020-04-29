function renderWorldStats(id) {
    const placeholder = document.querySelector(`#${id}`);
    fetch('/world', {
        method: 'GET'
    }).then(res => res.text())
    .then(html => document.getElementById(id).innerHTML = html);  
}