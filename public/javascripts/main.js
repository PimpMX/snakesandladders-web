$(document).ready(function () {

    connectWebSocket();
    attachListeners();

    function connectWebSocket() {
        const websocket = new WebSocket("ws://localhost:9000/websocket");
        websocket.setTimeout

        websocket.onopen = function (event) {
            console.debug("Connected to Websocket");
        }

        websocket.onclose = function () {
            console.debug('Connection with Websocket Closed!');
        };

        websocket.onerror = function (error) {
            console.debug('Error in Websocket Occured: ' + error);
        };

        websocket.onmessage = function (e) {

            const message = JSON.parse(e.data);
            console.log("Received message");
            console.debug(message);

            if(message.type === "redirect") {
                window.location.href = message.location;
            } else if(message.type === "htmlContent") {
                updateGameBoard(message.boardHtml);
            }
        };
    }

    function ajaxGetRequest(url, successCallback, errorCallback) {
        $.ajax({
            url: url,
            method: 'GET',
            success: successCallback,
            error: errorCallback
        });
    }

    // All possible requests in a dictionary => calling each one will cause the server side socket
    // actor to send a new board html, which we will then process in our websocket.onmessage handler

    const requests = {
        create: (dimensions) => {
            ajaxGetRequest(`/create/${dimensions}`, () => {
            }, () => {
                alert("Error creating the game")
            })
        },
        undo: () => {
            ajaxGetRequest(`/undo`, () => {
            }, () => {
                alert("Error undoing the last step")
            })
        },
        start: () => {
            ajaxGetRequest(`/start`, () => {
            }, () => {
                alert("Error starting game")
            })
        },
        restart: () => {
            ajaxGetRequest(`/restart`, () => {
            }, () => {
                alert("Error restarting game")
            })
        },
        roll: () => {
            ajaxGetRequest(`/roll`, () => {
            }, () => {
                alert("Error rolling")
            })
        },
        addPlayer: (playerName) => {
            ajaxGetRequest(`/add/${playerName}`, () => {
            }, () => {
                alert("Error adding new player")
            })
        }
    }

    function updateGameBoard(html) {
        $('#board').html($(html).find('#board').html());
        $('#playerList').html($(html).find('#playerList').html());
        $('#rollResult').html($(html).find('#rollResult').html());
        attachListeners();
    }

    function attachListeners() {

        function clearPlayerList() {
            $('#playerList').empty();
            $('#playerList').append('<li id="noPlayersMessage" class="list-group-item">No players added yet</li>');
        }

        // Detach and reattach listeners
        $('#undoButton').off('click').click(() => {
            console.log("undoing");
            requests.undo();
        });

        $('.create-game').off('click').click(function () {
            const dimensions = $(this).data('dimensions');
            requests.create(dimensions);
        });

        $('#startGameButton').off('click').click(() => {
            requests.start();
        });

        $('#restartGameButton').off('click').click(() => {
            requests.restart();
        });

        $('#rollDiceButton').off('click').click(() => {
            requests.roll();
        });

        function addPlayer() {
            const name = $('#playerName').val();
            if (!name) {
                alert("Please enter a player name");
                return;
            }
            requests.addPlayer(name);
        }

        $('#playerName').off('keydown').keydown((event) => {
            if (event.key !== "Enter")
                return;
            addPlayer();
        });

        $('#addPlayerButton').off('click').click(() => {
            addPlayer();
        });
    }
});
