$(document).ready(function() {

    $('#undoButton').click(function() {
        window.location.href = "/undo";
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
        if (playerName) {
            $.ajax({
                url: '/add/' + playerName,
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    $('#playerName').val('');

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

});


