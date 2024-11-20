export function connectWebSocket(onMessageCb) {
    const websocket = new WebSocket("ws://localhost:9000/api/websocket");

    websocket.onopen = function () {
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
        onMessageCb(message);
    };
}