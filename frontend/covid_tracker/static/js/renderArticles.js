function renderArticles(id) {
    const placeholder = document.querySelector(`#${id}`);
    fetch('/articles', {
        method: 'GET'
    }).then(res => res.text())
    .then(html => placeholder.innerHTML = html);
    
}