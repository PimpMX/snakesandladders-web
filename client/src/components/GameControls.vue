<script>
import {requests} from "@/util/requests";

export default {
  name: 'GameControls',
  props: {},
  methods: {
    exitGame() {
      requests.restart();
    },
    undoMove() {
      requests.undo();
    },
    roll() {
      requests.roll()
    }
  }
}
</script>

<template>
  <div id="gameControls" class="controls mb-2">
    <button id="restartGameButton" class="btn btn-secondary" @click="exitGame()">Exit</button>
    <button id="undoButton" class="btn btn-secondary" @click="undoMove()">Undo</button>
    <button id="rollButton" class="btn btn-secondary" @click="roll()">Roll</button>
  </div>
</template>

<style scoped>
</style>
