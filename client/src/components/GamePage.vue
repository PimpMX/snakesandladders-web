<template>
  <GameControls :state="state" @rollDice="handleDiceRoll" @undoMove="undoMove" />
  <PlayerList :players="state.players" :currentPlayerIndex="currentPlayerIndex" />
  <GameField :state="state" />

  <!-- Modal for displaying winner notification -->
  <div v-if="winner" class="winner-modal">
    <div class="modal-content">
      <h2>🎉 {{ winner }} won the game! 🎉</h2>
      <button class="btn btn-primary" @click="exitGame">Exit</button>
    </div>
  </div>
</template>

<script>
import GameControls from "@/components/GameControls.vue";
import GameField from "@/components/GameField.vue";
import PlayerList from "@/components/PlayerList.vue";
import { requests } from "@/util/requests";

export default {
  name: 'GamePage',
  props: {
    state: Object
  },
  data() {
    return {
      currentPlayerIndex: 0,
      previousStates: [],
      winner: null, // To store the winner's name
    };
  },
  methods: {
    async handleDiceRoll(diceValue) {
      try {
        await requests.roll();

        const stateCopy = JSON.parse(JSON.stringify(this.state));
        this.previousStates.push(stateCopy);

        const currentPlayer = this.state.players[this.currentPlayerIndex];
        currentPlayer.position += diceValue;

        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.state.players.length;

        this.$emit('updateState', this.state);

        // Check for a winner after the dice roll
        const winResponse = await requests.checkWin();
        if (winResponse?.winner) {
          this.winner = winResponse.winner; // Set winner's name
        }

        this.$forceUpdate();
      } catch (error) {
        console.error("Error during dice roll:", error);
      }
    },
    async undoMove() {
      if (this.previousStates.length > 0) {
        const lastState = this.previousStates.pop();

        try {
          await requests.undo();
        } catch (error) {
          console.error("Error during undo:", error);
          return;
        }

        this.$emit('updateState', lastState);

        this.currentPlayerIndex = (this.currentPlayerIndex - 1 + this.state.players.length) % this.state.players.length;

        this.$forceUpdate();
      }
    },
    exitGame() {
      requests.restart(); // Restart the game
      this.winner = null; // Clear winner notification
    },
  },
  components: { GameField, GameControls, PlayerList },
};
</script>

<style scoped>
.winner-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  text-align: center;
  z-index: 1000;
}

.modal-content h2 {
  margin-bottom: 20px;
}

.modal-content .btn {
  padding: 10px 20px;
  font-size: 1rem;
}
</style>
