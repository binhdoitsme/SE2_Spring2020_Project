$('input[type=checkbox]').on('change', (event) => {
    const target = event.currentTarget;
    handlers[target.id](target);
})
const resultContainer = document.querySelector('#result-container');