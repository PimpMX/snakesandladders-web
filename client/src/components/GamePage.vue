<script>
import GameControls from "@/components/GameControls.vue";
import GameField from "@/components/GameField.vue";

export default {
  name: 'GamePage',
  props: {},
  methods: {},
  components: {GameField, GameControls}
}
</script>

<template>
  <GameControls />
  <GameField />
</template>

<style scoped>

</style>