<template>
  <div id="gameControls" class="controls mb-2">
    <button id="restartGameButton" class="btn btn-secondary" @click="exitGame()">Exit</button>
    <button id="undoButton" class="btn btn-secondary" @click="undoMove()">Undo</button>
  </div>


  <div class="dice-container">
    <DiceComponent ref="dice" @diceRolled="handleDiceRoll" />
  </div>
</template>

<script>
import { requests } from "@/util/requests";
import DiceComponent from "@/components/DiceComponent.vue";

export default {
  name: 'GameControls',
  components: {
    DiceComponent
  },
  methods: {
    exitGame() {
      requests.restart();
    },
    undoMove() {
      this.$emit('undoMove');
    },
    handleDiceRoll(diceValue) {
      this.$emit('rollDice', diceValue);
    }
  }
}
</script>

<style scoped>
.controls {
  display: flex;
  margin-bottom: 20px;
}

.dice-container {
  position: absolute;
  top: 50%;
  right: 20%;
  transform: translateY(-50%);
  width: 10vw;
  height: 10vw;
}

.dice-container img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>
