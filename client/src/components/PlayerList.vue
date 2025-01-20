<template>
  <div id="PlayerList">
    <ul v-if="players.length === 0" class="list-group list-group-horizontal">
      <li class="list-group-item">No players in the lobby yet</li>
    </ul>
    <ul v-if="players.length > 0" class="list-group list-group-horizontal">
      <li v-for="(player, index) in players" :key="index" class="list-group-item player-item">
        <!-- Display player's name with their color and position -->
        <span v-if="player.name !== this.localPlayerName" :style="{ color: player.color }">{{ player.name }} ({{ player.position }})</span>
        <span v-if="player.name === this.localPlayerName" :style="{ color: player.color }">{{ player.name }} (You)</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'PlayerList',
  props: {
    players: Array,
    localPlayerName: String // Player name of our current client
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