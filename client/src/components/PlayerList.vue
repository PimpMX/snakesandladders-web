<template>
  <div id="PlayerList" class="container">
    <ul class="list-group list-group-horizontal">
      <li v-if="players.length === 0" class="list-group-item">No players added yet</li>

      <li v-for="(player, index) in players" :key="index" class="list-group-item PlayerItem"
          :class="{ 'list-group-item-info': index === currentPlayerIndex }">
        <span :style="{ fontWeight: index === currentPlayerIndex ? 'bold' : 'normal', color: player.color }">
          {{ player.name }} ({{ player.position }})
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'PlayerList',
  props: {
    players: Array,
    currentPlayerIndex: Number
  }
}
</script>

<style scoped>
#PlayerList {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding-bottom: 15px;
}

.list-group-item-info {
  background-color: #13a7d6 !important;
}
.PlayerItem{
  background-color: #8fd4ea;
}
</style>
