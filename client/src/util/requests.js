export function fetchRequest(url, options = {}) {
    return fetch(url, options)
        .then(response => {
            const responseDate = response.headers.get('Date');
            const responseDateTime = responseDate ? new Date(responseDate).getTime()  : null;
            const currentTime = new Date().getTime();
            if (responseDateTime && (currentTime - responseDateTime) > 5000) {
                window.isCacheResponse = true;
                console.log('Response appears to be from cache, based on Date header:', url);
            } else {
                window.isCacheResponse = false;
                console.log('Response fetched from network:', url);
            }
            if (!response.ok) {
                throw new Error(`Request failed with status ${response.status}`);
            }
            if (response.status === 204) {
                return Promise.resolve();
            }
            return response.json(); // or other response handling logic
        })
        .catch(error => {
            console.error("Error:", error);
            throw error;
        });
}

// All possible requests in a dictionary => calling each one will cause the server side socket
// actor to send a new board HTML, which we will then process in our websocket.onmessage handler

export const requests = {
    async state() {
        return fetchRequest("/api/state");
    },
    create(dimensions) {
        fetchRequest(`/api/create/${dimensions}`, {
            method: "GET",
        }).catch(() => {
            alert("Error creating the game");
        });
    },
    undo() {
        fetchRequest(`/api/undo`, {
            method: "GET",
        }).catch(() => {
            alert("Error undoing the last step");
        });
    },
    start() {
        fetchRequest(`/api/start`, {
            method: "GET",
        }).catch(() => {
            alert("Error starting game");
        });
    },
    restart() {
        fetchRequest(`/api/restart`, {
            method: "GET",
        }).catch(() => {
            alert("Error restarting game");
        });
    },
    roll() {
        return fetchRequest(`/api/roll`, {
            method: "GET",
        }).catch(() => {
            alert("Error rolling");
        });
    },
    addPlayer(playerName) {
        fetchRequest(`/api/add/${playerName}`, {
            method: "GET",
        }).catch(() => {
            alert("Error adding new player");
        });
    },
    async checkWin() {
        return fetchRequest(`/api/checkWin`, {
            method: "GET",
        });
    },
};

