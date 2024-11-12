$(document).ready(function() {

    $('#undoButton').click(function() {
        window.location.href = "/undo";
    });

    $('.create-game').click(function() {
        var dimensions = $(this).data('dimensions');
        window.location.href = `create/${dimensions}`;
    });

    $('#startGameButton').click(function() {
        window.location.href = "/start";
    });

    $('#restartGameButton').click(function() {
        window.location.href = "/restart";
    });

    $('#rollDiceButton').click(function() {
        window.location.href = "/roll";
    });

    $('#playerName').keydown(function(event) {
        if (event.key === "Enter") {
            addPlayer();
        }
    });

    $('#addPlayerButton').click(function() {
        addPlayer();
    });

function addPlayer() {
    var playerName = $('#playerName').val();
    window.location.href = `/add/${playerName}`;
}

});


