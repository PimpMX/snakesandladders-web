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
      selectedSize: null
    }
  },
  methods: {
    startGame() {
      requests.start();
    },
    createBoard(dimensions) {
      this.selectedSize = dimensions;
      requests.create(dimensions);
    },
    addPlayer(playerName) {
      requests.addPlayer(playerName);
      this.playerName = "";
    }
  },
  components: {
    PlayerList
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
            :class="['btn', 'btn-success', { 'btn-lightblue': selectedSize === 8 }]"
            @click="createBoard(8)"
        >
          8x8
        </button>
        <button
            :class="['btn', 'btn-success', { 'btn-lightblue': selectedSize === 10 }]"
            @click="createBoard(10)"
        >
          10x10
        </button>
        <button
            :class="['btn', 'btn-success', { 'btn-lightblue': selectedSize === 12 }]"
            @click="createBoard(12)"
        >
          12x12
        </button>
      </div>

      <!-- Message is always present in the DOM, but hidden when not needed -->
      <div v-show="!selectedSize" class="warning-message mt-3">
        You must select a board size
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

      <PlayerList :players="state.players"/>
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
