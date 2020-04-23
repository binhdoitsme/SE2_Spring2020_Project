function renderRecordByPoi(id) {
    const placeholder = document.querySelector(`#${id}`);
    fetch('/stats', {
        method: 'GET'
    }).then(res => res.text())
    .then(html => placeholder.innerHTML = html);   
}