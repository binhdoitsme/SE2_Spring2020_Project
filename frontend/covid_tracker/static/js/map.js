function getMap(id) {
    const map = document.querySelector(`#${id}`);
    fetch('/map', {
        method: 'GET'
    }).then((req.res))
}