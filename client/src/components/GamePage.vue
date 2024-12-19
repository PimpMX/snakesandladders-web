<script>
import GameControls from "@/components/GameControls.vue";
import GameField from "@/components/GameField.vue";
import PlayerList from "@/components/PlayerList.vue";


export default {
  name: 'GamePage',
  props: {
    state: Object
  },
  methods: {},
  components: {GameField, GameControls, PlayerList}
}
</script>

<template>
  <GameControls />
  <PlayerList :players="state.players" />
  <GameField :state="state" />

</template>

<style scoped>

</style>