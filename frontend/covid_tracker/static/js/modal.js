function showLoadingModal() {
    const modal = document.createElement('div');
    modal.innerHTML = `
    <div id="loading-modal" class="h-100 w-100 d-flex justify-content-center align-items-center" style="width: 100vw;height: 100vh;background: rgba(0, 0, 0, 0.8);position: absolute;top: 0;z-index: 9999;">
        <div class="spinner-border text-warning" role="status"></div>
    </div>
    `;
    modal.id = "loading-modal";
    modal.classList.add('d-flex');
    modal.classList.add('justify-content-center');
    modal.classList.add('align-items-center');
    modal.style.background = 'rgba(255, 255, 255, 0.4)';
    document.body.appendChild(modal);
}

function showSolidLoadingModal() {
    const modal = document.createElement('div');
    modal.innerHTML = `
    <div id="loading-modal" class="h-100 w-100 d-flex justify-content-center align-items-center bg-dark" style="width: calc(100vw - 3.5rem);height: 100vh;position: absolute;top: 0;z-index: 9999;">
        <h4 class='text-light mr-2'>Preparing data...</h4>
        <div class="spinner-border text-warning" role="status"></div>
    </div>
    `;
    modal.id = "loading-modal";
    modal.classList.add('d-flex');
    modal.classList.add('justify-content-center');
    modal.classList.add('align-items-center');
    modal.style.background = 'rgba(255, 255, 255, 0.4)';
    document.body.appendChild(modal);
}

function removeLoadingModal() {
    document.querySelector('#loading-modal').remove();
}

function showMessageModal(bootstrapClass, title, message, modalToRemove='addLocationQuick', dismissCallback=null) {
    const div = document.createElement('div');
//    <div class="modal fade" id="status_modal" tabindex="-1" role="dialog" aria-labelledby="modelTitleId" aria-hidden="true">
    div.classList.add('modal');
    div.classList.add('fade');
    div.id = 'status_modal';
    div.attributes.tabIndex = '-1';
    div.setAttribute('role', 'dialog');
    div.setAttribute('aria-modal', 'true');
    div.style.zIndex = '9999';
    div.innerHTML =  `
    <!-- Modal -->
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${title}</h5>
                </div>
                <div class="modal-body">
                    ${message}
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-${bootstrapClass}" data-dismiss="modal">Dismiss</button>
                </div>
            </div>
        </div>
    <script>
        $('#status_modal').on('hidden.bs.modal', (event) => {
            $('#status_modal').remove();
        });
    </script>`;
    document.body.appendChild(div);
    $('#status_modal').modal('show');
    $(`#${modalToRemove}`).modal('hide');
    if (dismissCallback !== null) {
        $('#status_modal').on('hide.bs.modal', dismissCallback);
    }
}