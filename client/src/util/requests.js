export function fetchRequest(url, options = {}) {
    return fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Request failed with status ${response.status}`);
            }
            if(response.status === 204)
                return Promise.resolve();
            return response.json();
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
            method: 'GET',
        }).catch(() => {
            alert("Error creating the game");
        });
    },
    undo() {
        fetchRequest(`/api/undo`, {
            method: 'GET',
        }).catch(() => {
            alert("Error undoing the last step");
        });
    },
    start() {
        fetchRequest(`/api/start`, {
            method: 'GET',
        }).catch(() => {
            alert("Error starting game");
        });
    },
    restart() {
        fetchRequest(`/api/restart`, {
            method: 'GET',
        }).catch(() => {
            alert("Error restarting game");
        });
    },
    roll() {
        fetchRequest(`/api/roll`, {
            method: 'GET',
        }).catch(() => {
            alert("Error rolling");
        });
    },
    addPlayer(playerName) {
        fetchRequest(`/api/add/${playerName}`, {
            method: 'GET',
        }).catch(() => {
            alert("Error adding new player");
        });
    }
};