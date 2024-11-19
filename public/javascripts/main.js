$(document).ready(function() {

    connectWebSocket();

    function connectWebSocket() {
        var websocket = new WebSocket("ws://localhost:9000/websocket");
        websocket.setTimeout

        websocket.onopen = function(event) {
            console.log("Connected to Websocket");
        }

        websocket.onclose = function () {
            console.log('Connection with Websocket Closed!');
        };

        websocket.onerror = function (error) {
            console.log('Error in Websocket Occured: ' + error);
        };

        websocket.onmessage = function (e) {
            console.log(e);
        };
    }

    $('#undoButton').click(function() {
        $.ajax({
            url: "/undo",
            method: "GET",
            dataType: "json",
            success: function(response) {
                $('#board').html($(response.boardHtml).find('#board').html());
                $('#playerList').html($(response.boardHtml).find('#playerList').html());
                $('#rollResult').html($(response.boardHtml).find('#rollResult').html());

                attachRollDiceListener();
            },
            error: function() {
                alert("Error undoing the last step.");
            }
        });
    });

    function clearPlayerList() {
        $('#playerList').empty();
        $('#playerList').append('<li id="noPlayersMessage" class="list-group-item">No players added yet</li>');
    }

    $('.create-game').click(function() {
        var dimensions = $(this).data('dimensions');

        $.ajax({
            url: '/create/' + dimensions,
            method: 'GET',
            dataType: 'json',
            success: function(response) {
                if (response.success) {
                    $('#gameCreationMessage').html('<div class="alert alert-success">' + response.message + '</div>');
                    clearPlayerList();
                } else {
                    $('#gameCreationMessage').html('<div class="alert alert-danger">Game creation failed!</div>');
                }
            },
            error: function () {
                alert("Error creating the game.");
            }
        });
    });

    $('#startGameButton').click(function() {
        window.location.href = "/start";
    });

    $('#restartGameButton').click(function() {
        window.location.href = "/restart";
    });

    function attachRollDiceListener() {
        $('#rollDiceButton').click(function() {
            $.ajax({
                url: "/roll",
                method: "GET",
                dataType: "json",
                success: function(response) {
                    $('#board').html($(response.boardHtml).find('#board').html());
                    $('#playerList').html($(response.boardHtml).find('#playerList').html());
                    $('#rollResult').html($(response.boardHtml).find('#rollResult').html());

                    attachRollDiceListener();
                },
                error: function() {
                    alert("Error rolling the dice.");
                }
            });
        });
    }

    attachRollDiceListener();

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
        if (playerName) {
            $.ajax({
                url: '/add/' + playerName,
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    $('#playerName').val(''); // Clear the input field

                    var playerList = "";
                    data.players.forEach(function(player) {
                        playerList += '<li class="list-group-item">' + player + '</li>';
                    });
                    $('#playerList').html(playerList);
                },
                error: function(error) {
                    console.error('Error adding player:', error);
                }
            });
        } else {
            alert('Please enter a player name.');
        }
    }

    function updateGameBoard(boardHtml, playersListHtml, rolledValue) {
        $('.board-container').html(boardHtml)
        $('#playerList').html(playersListHtml);
        $('#rolledValue').text('Rolled: ' + rolledValue);
    }
});
