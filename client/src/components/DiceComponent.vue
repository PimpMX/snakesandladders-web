<template>
  <div class="ms-5 dice-container d-flex flex-column">
    <img :src="diceImage" alt="Dice" style="width: 4vw; height: 4vw"/>
    <button @click="rollDice" type="button" class="dice-button"
            :disabled="this.$route.query.playerName !== this.state.currentPlayer.name">
      {{ rolling ? "Rolling" : "Roll" }}
    </button>
  </div>
</template>

<script>
export default {
  name: "DiceComponent",
  props: {
    state: Object
  },
  data() {
    return {
      diceImages: [
        require("@/assets/dice_1.png"),
        require("@/assets/dice_2.png"),
        require("@/assets/dice_3.png"),
        require("@/assets/dice_4.png"),
        require("@/assets/dice_5.png"),
        require("@/assets/dice_6.png"),
      ],
      diceImage: require("@/assets/dice_1.png"),
      rolling: false,
      finalValue: 1,
    };
  },
  methods: {
    rollDice() {
      if (this.rolling) return;
      this.rolling = true;
      let rollCount = 0;

      const rollInterval = setInterval(() => {
        this.diceImage = this.diceImages[Math.floor(Math.random() * 6)];
        rollCount++;

        if (rollCount >= 10) {
          clearInterval(rollInterval);

          this.finalValue = Math.floor(Math.random() * 6) + 1;
          this.diceImage = this.diceImages[this.finalValue - 1];
          this.rolling = false;

          this.$emit("diceRolled", this.finalValue);
        }
      }, 100);
    },
  },
};
</script>

<style scoped>
.dice-container {
  text-align: center;
  margin-top: 20px;
}

.dice-container img {
  width: 100px;
  height: 100px;
  margin-bottom: 10px;
}

.dice-button {
  background-color: #4caf50;
  color: white;
  font-size: 1rem;
  border: none;
  border-radius: 5px;
  width: 4vw;
  height: 2vw;
}

.dice-button:hover {
  background-color: #45a049;
}

.dice-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>