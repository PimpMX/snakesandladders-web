<script>
import {requests} from "@/util/requests";

export default {
  name: 'IndexPage',
  props: {
    state: Object
  },
  data() {
    return {
      playerName: ""
    }
  },
  methods: {
    startGame() {
      requests.start();
    },
    createBoard(dimensions) {
      requests.create(dimensions);
    },
    addPlayer(playerName) {
      requests.addPlayer(playerName)
      this.playerName = "";
    }
  }
}
</script>

<template>
  <div class="container">
    <h1>Snakes and Ladders</h1>
    <p>
      The game of Snakes and Ladders is played between two or more players on a board with numbered squares.
      The objective is to navigate your token from start to finish by rolling dice.
    </p>
    <p>
      If you land on a ladder, you move up to a higher number. If you land on a snake, you slide down to a lower number.
    </p>

    <section class="recommended-sizes">
      <h2 class="col-md-5">Recommended Board Sizes</h2>
      <div class="button-group mt-3">
        <button class="btn btn-success create-game" @click="createBoard(8)">8x8</button>
        <button class="btn btn-success create-game" @click="createBoard(10)">10x10</button>
        <button class="btn btn-success create-game" @click="createBoard(12)">12x12</button>
      </div>

      <div id="gameCreationMessage" class="mt-3"></div>
    </section>

    <section>
      <h2 class="col-md-5">Add Players</h2>
      <div class="input-group justify-content-center mt-3">
        <input id="playerName" v-model="playerName" class="input-group-text col-md-3"
               @keydown.enter="addPlayer(playerName)" type="text" placeholder="Player Name" required/>
        <button id="addPlayerButton" class="btn btn-success" @click="addPlayer(playerName)">Add</button>
      </div>

      <div class="controls d-flex justify-content-center align-items-center">
        <ul v-if="this.state.players.length == 0" id="playerList" class="list-group list-group-horizontal">
          <li id="noPlayersMessage" class="list-group-item">No players added yet</li>
        </ul>
        <ul v-if="this.state.players.length > 0" id="playerList" class="list-group list-group-horizontal">
          <li v-for="(player, index) in this.state.players" :key="index" id="noPlayersMessage" class="list-group-item">
            {{ player.name }}
          </li>
        </ul>
      </div>
    </section>

    <section>
      <button id="startGameButton" class="btn btn-primary col-md-2" @click="startGame">Start</button>
    </section>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
</style>