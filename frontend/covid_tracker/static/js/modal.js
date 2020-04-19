function showLoadingModal() {
    const modal = document.createElement('div');
    modal.innerHTML = `
    <div id="loading-modal" class="d-flex justify-content-center align-items-center" style="width: 100vw;height: 100vh;background: rgba(0, 0, 0, 0.8);position: absolute;top: 0;z-index: 9999;">
        <div class="spinner-border text-warning" role="status"></div>
    </div>
    `;
    modal.id = "loading-modal";
    modal.classList.add('d-flex');
    modal.classList.add('justify-content-center');
    modal.classList.add('align-items-center');
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.background = 'rgba(255, 255, 255, 0.4)';
    document.body.appendChild(modal);
}

function removeLoadingModal() {
    document.querySelector('#loading-modal').remove();
}
