export function connectWebSocket(onMessageCb) {

    const connectUrl = window.location.protocol === "http:" ? `ws://${window.location.hostname}/api/websocket`:
        `wss://${window.location.hostname}/api/websocket`
    const websocket = new WebSocket(connectUrl);

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
        console.debug("Received Message");
        console.debug(message);
        onMessageCb(message);
    };
}