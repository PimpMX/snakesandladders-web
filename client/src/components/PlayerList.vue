<template>
  <div id="PlayerList">
    <ul v-if="players.length === 0" class="list-group list-group-horizontal">
      <li class="list-group-item">No players added yet</li>
    </ul>
    <ul v-if="players.length > 0" class="list-group list-group-horizontal">
      <li v-for="(player, index) in players" :key="index" class="list-group-item player-item">
        <span :style="{ color: player.color }">{{ player.name }} ({{ player.position }})</span>
        <!-- Display player's name with their color and position -->
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'PlayerList',
  props: {
    players: Array
  }
}
</script>

<style scoped>
#PlayerList {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding-top: 10px;
  padding-bottom: 15px;
}

.player-item {
  background-color: lightblue;
}
</style>
