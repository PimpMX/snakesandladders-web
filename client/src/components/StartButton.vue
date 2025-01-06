<template>
  <button
      v-if="show"
      id="startGameButton"
      class="btn btn-primary col-md-2"
      @click="startGame"
  >
    Start Game
  </button>
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
