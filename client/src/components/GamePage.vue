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
      previousStates: []
    };
  },
  methods: {
    async handleDiceRoll(diceValue) {
      // Send the roll request to the server
      try {
        await requests.roll();  // This will trigger the backend roll logic

        // Update the player's position after the dice roll
        const stateCopy = JSON.parse(JSON.stringify(this.state));
        this.previousStates.push(stateCopy);

        const currentPlayer = this.state.players[this.currentPlayerIndex];
        currentPlayer.position += diceValue;  // Update the player's position with dice value

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
    }
  },
  components: {GameField, GameControls, PlayerList}
}
</script>

<template>
  <GameControls :state="state" @rollDice="handleDiceRoll" @undoMove="undoMove"/>
  <PlayerList :players="state.players" :currentPlayerIndex="currentPlayerIndex"/>
  <GameField :state="state"/>
</template>
