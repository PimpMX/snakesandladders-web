<script>
import GameControls from "@/components/GameControls.vue";
import GameField from "@/components/GameField.vue";

export default {
  name: 'GamePage',
  props: {
    state: Object
  },
  methods: {},
  components: {GameField, GameControls}
}
</script>

<template>
  <GameControls />
  <GameField :state="state" />
</template>

<style scoped>

</style>