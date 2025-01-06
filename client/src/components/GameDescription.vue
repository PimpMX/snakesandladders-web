<template>
  <div class="game-description">
    <h1>Snakes and Ladders</h1>
    <p>
      The game of Snakes and Ladders is played between two or more players on a board with numbered squares.
      The objective is to navigate your token from start to finish by rolling dice.
    </p>
    <p>
      If you land on a ladder, you move up to a higher number. If you land on a snake, you slide down to a lower number.
    </p>
  </div>
</template>

<script>
export default {
  name: "GameDescription",
};
</script>

<style scoped>
.game-description {
  text-align: center;
  margin-bottom: 20px;
}

h1 {
  font-size: 2.5rem;
  color: #ffffff;
}

p {
  font-size: 1rem;
  line-height: 1.5;
  color: #ffffff;
}
</style>
