// event handler here
const togglableHandler = (target) => {
    // todo: handle the event!
    if (target.checked) {
        $(`#${target.id}_result`).show();
    } else {
        $(`#${target.id}_result`).hide();
    }
};
const handlers = {
    // id : handler
    worldLatest: togglableHandler,
    worldTrend: togglableHandler,
    worldMap: togglableHandler,
    countryTop10: togglableHandler,
    caseDistribution: togglableHandler,
    vietnameseDistribution: togglableHandler,
    vietnameseLatest: togglableHandler,
    vietnameseTable: togglableHandler,
    continentLatest: togglableHandler,
    continentTrends: togglableHandler
}