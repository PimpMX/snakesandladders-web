<template>
  <GameControls :state="state" @rollDice="handleDiceRoll" @undoMove="undoMove" />
  <PlayerList :players="state.players" :currentPlayerIndex="currentPlayerIndex" />
  <GameField :state="state" />

  <!-- Winner Banner -->
  <div v-if="winner" class="winner-banner">
    <h2>🎉 {{ winner }} has won the game! 🎉</h2>
    <button class="btn btn-primary" @click="exitGame">Exit</button>
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
    state: Object,
  },
  data() {
    return {
      currentPlayerIndex: 0,
      previousStates: [],
      winner: null, // Winner name
    };
  },
  mounted() {
    // Start polling for a winner
    this.pollWinner();
  },
  methods: {
    async handleDiceRoll(diceValue) {
      try {
        await requests.roll();

        // Update game state locally
        const stateCopy = JSON.parse(JSON.stringify(this.state));
        this.previousStates.push(stateCopy);

        const currentPlayer = this.state.players[this.currentPlayerIndex];
        currentPlayer.position += diceValue;

        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.state.players.length;
        this.$emit('updateState', this.state);

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
    async exitGame() {
      await requests.restart(); // Restart game
      this.winner = null; // Clear winner banner
    },
    async pollWinner() {
      try {
        setInterval(async () => {
          const response = await requests.checkWin();
          if (response?.winner && response.winner !== null) {
            this.winner = response.winner; // Set the winner and display banner
          }
        }, 2000); // Check every 2 seconds
      } catch (error) {
        console.error("Error checking winner:", error);
      }
    },
  },
  components: { GameField, GameControls, PlayerList },
};
</script>

<style scoped>
.winner-banner {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(152, 82, 82, 0.9);
  padding: 20px 40px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 9999;
}

.winner-banner h2 {
  font-size: 2rem;
  margin-bottom: 20px;
}

.winner-banner .btn {
  font-size: 1.2rem;
  padding: 10px 20px;
}
</style>
