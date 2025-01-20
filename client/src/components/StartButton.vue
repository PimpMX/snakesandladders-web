<template>
  <v-btn
      v-if="show"
      id="startGameButton"
      color="primary"
      class="start-game-btn"
      @click="startGame"
  >
    Start Game
  </v-btn>
</template>

<script>
export default {
  name: "StartButton",
  props: {
    show: Boolean,
  },
  methods: {
    startGame() {
      this.$emit("startGame");
    },
  },
};
</script>

<style scoped>
.start-game-btn {
  font-size: 18px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  text-transform: uppercase;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.start-game-btn:hover {
  transform: scale(1.05);
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.3);
}
</style>
