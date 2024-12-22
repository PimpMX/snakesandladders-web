<template>
  <div class="dice-container">
    <img :src="diceImage" alt="Dice" />
  </div>
</template>

<script>
export default {
  name: 'DiceComponent',
  data() {
    return {
      diceImages: [
        require('@/assets/dice_1.png'),
        require('@/assets/dice_2.png'),
        require('@/assets/dice_3.png'),
        require('@/assets/dice_4.png'),
        require('@/assets/dice_5.png'),
        require('@/assets/dice_6.png')
      ],
      diceImage: require('@/assets/dice_1.png'),
      rolling: false,
      finalValue: 1
    };
  },
  methods: {
    rollDice() {
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

          // Emit the rolled dice value to the parent component
          this.$emit('diceRolled', this.finalValue);
        }
      }, 100);
    }
  }
};
</script>

<style scoped>
.dice-container img {
  width: 100px;
  height: 100px;
}
</style>
