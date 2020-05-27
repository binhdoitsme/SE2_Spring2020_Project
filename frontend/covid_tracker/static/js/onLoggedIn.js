$(document).unbind('keyup');
const form = document.querySelector('#login-form');
$('#loginModal').on('hidden.bs.modal', (event) => {
    event.currentTarget.querySelector('#login-form').reset();
});
form.addEventListener('submit', (event) => {
    event.preventDefault();
    showLoadingModal();
    login().then(statusCode => {
        if (statusCode === 200) {
            $('#loginModal').modal('hide');
            fetch('/?page=dashboard', { method: 'POST' }).then(res => {
                window.location.reload();
            });
        } else {
            setTimeout(removeLoadingModal, 100);
            showMessageModal('danger', 'Login failed', 'Your credentials are undefined for this system, please try again!', 'loginModal');
        }
    });
});