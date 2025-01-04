<script>
import { requests } from "@/util/requests";
import PlayerList from "@/components/PlayerList.vue";  // Import the PlayerList component

export default {
  name: 'IndexPage',
  props: {
    state: Object
  },
  data() {
    return {
      playerName: "",
    }
  },
  methods: {
    startGame() {
      // Check if a player has been added to the players list and if a board size is selected
      if (this.state.players.length === 0) {
        alert("You must select a board size and need at least one player in the lobby before starting the game!");
        return; // Don't proceed with starting the game
      }
      // Proceed with the game start if validation passes
      requests.start();
    },
    createBoard(dimensions) {
      requests.create(dimensions);
      this.$router.push({
        path: this.$route.path,
        query: {}
      })
    },
    confirmPlayerName(playerName) {
      // Add player to the players list
      if (playerName.trim() !== "") {
        requests.addPlayer(playerName);
        this.$router.push({
          path: this.$route.path,
          query: { ...this.$route.query, playerName: this.playerName }
        });
      }
    }
  },
  components: {
    PlayerList  // Register PlayerList as a child component
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
        <button
            :class="['btn', 'btn-success', { 'btn-lightblue': Math.sqrt(state.board.dimensions) === 8 }]"
            @click="createBoard(8)"
        >
          8x8
        </button>
        <button
            :class="['btn', 'btn-success', { 'btn-lightblue': Math.sqrt(state.board.dimensions) === 10 }]"
            @click="createBoard(10)"
        >
          10x10
        </button>
        <button
            :class="['btn', 'btn-success', { 'btn-lightblue': Math.sqrt(state.board.dimensions) === 12 }]"
            @click="createBoard(12)"
        >
          12x12
        </button>
      </div>

      <!-- Message is always present in the DOM, but hidden when not needed -->
      <div v-show="Math.sqrt(state.board.dimensions) !== 8 &&
        Math.sqrt(state.board.dimensions) !== 10 &&
        Math.sqrt(state.board.dimensions) !== 12" class="warning-message mt-3">
        You must select a board size
      </div>

      <div id="gameCreationMessage" class="mt-3"></div>
    </section>

    <section>
      <h2 class="col-md-3">The Lobby</h2>
      <PlayerList class="mt-2" :players="state.players" :local-player-name="this.playerName"/>
    </section>

    <section v-if="!this.$route.query.playerName">
      <h2 class="col-md-4">Choose Your Name</h2>
      <div class="input-group justify-content-center mt-3">
        <input id="playerName" v-model="playerName" class="input-group-text col-md-2"
               @keydown.enter="confirmPlayerName(playerName)" type="text" placeholder="Your Player Name" required/>
        <button id="addPlayerButton" class="btn btn-success" @click="confirmPlayerName(playerName)">Confirm</button>
      </div>
    </section>

    <button v-if="this.$route.query.playerName" id="startGameButton" class="btn btn-primary col-md-2" @click="startGame">Start Game</button>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.btn-lightblue {
  background-color: #18a1ca !important;
  color: black !important;
  outline-color: #18a1ca;
}

.warning-message {
  color: #ffdd8f;
  font-weight: bold;
  text-align: center;
  font-size: 14px;
}
</style>
