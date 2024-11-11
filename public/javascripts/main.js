function undoLastStep() {
    window.location.href = "/undo";
}

function createGame(dimensions) {
    window.location.href= `create/${dimensions}`;
}

function start() {
    window.location.href = "/start";
}

function restart() {
    window.location.href = "/restart";
}

function rollDice() {
    window.location.href = "/roll";
}

function onInputFieldKeydown(event) {
    if(event.key === "Enter") {
        addPlayer();
    }
}

function addPlayer() {
    const nameInputField = document.getElementById("playerName");
    const playerName = nameInputField.value;
    window.location.href = `/add/${playerName}`;
}


