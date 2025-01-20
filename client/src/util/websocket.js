export function connectWebSocket(onMessageCb) {

    const websocket = new WebSocket(`wss://${window.location.hostname}/api/websocket`);

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