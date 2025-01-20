<template>
  <section>
    <h2 class="col-md-3">The Lobby</h2>
    <PlayerList class="mt-2" :players="players" :local-player-name="localPlayerName"/>
  </section>
</template>

<script>
import PlayerList from "@/components/PlayerList.vue";

export default {
  name: "PlayerLobby",
  props: {
    players: Array,
    localPlayerName: String,
  },
  components: {
    PlayerList,
  },
};

</script>
