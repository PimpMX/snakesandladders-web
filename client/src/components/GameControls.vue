<template>
  <div id="gameControls" class="controls mb-2">
    <button id="restartGameButton" class="btn btn-secondary" @click="exitGame()">Exit</button>
    <button id="undoButton" class="btn btn-secondary" @click="undoMove()">Undo</button>
  </div>

  <div class="dice-container align-items-center">
    <DiceComponent :state="state" ref="dice" @diceRolled="handleDiceRoll" />
  </div>
</template>

<script>
import { requests } from "@/util/requests";
import DiceComponent from "@/components/DiceComponent.vue";

export default {
  name: 'GameControls',
  props: {
    state: Object
  },
  components: {DiceComponent},
  methods: {
    exitGame() {
      requests.restart();
    },
    async undoMove() {
      try {
        // Call the undo function on the server
        await requests.undo();

        this.$emit('updateState', this.state);
        this.$forceUpdate();
      } catch (error) {
        console.error("Error during undo:", error);
      }
    },
    async handleDiceRoll(diceValue) {
      try {
        await requests.roll();

        // Update the player's position after the dice roll
        const currentPlayer = this.state.players[this.currentPlayerIndex];
        currentPlayer.position += diceValue;

        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.state.players.length;

        this.$emit('updateState', this.state);
        this.$forceUpdate();
      } catch (error) {
        console.error("Error during dice roll:", error);
      }
    }
  }
}
</script>

<style scoped>
.controls {
  display: flex;
  margin-bottom: 20px;
}

.dice-container img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.dice-container {
  position: absolute;
  top: 57%;
  right: 18%;
  transform: translateY(-50%);
  width: 10vw;
  height: 10vw;
}
</style>
