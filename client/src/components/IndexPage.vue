<template>
  <div class="container">
    <GameDescription/>
    <RecommendedSizes
        :selectedSize="Math.sqrt(state.board.dimensions)"
        @createBoard="createBoard"
    />
    <PlayerLobby :players="state.players" :local-player-name="playerName"/>
    <NameInput
        v-if="!$route.query.playerName"
        :playerName="playerName"
        @confirmPlayerName="confirmPlayerName"
        @update:playerName="playerName = $event"
    />
    <StartButton
        v-if="$route.query.playerName"
        :show="state.players.length > 0"
        @startGame="startGame"
    />
  </div>
</template>

<script>
import GameDescription from "./GameDescription.vue";
import RecommendedSizes from "@/components/RecommendedSizes.vue";
import PlayerLobby from "@/components/PlayerLobby.vue";
import NameInput from "@/components/NameInput.vue";
import StartButton from "@/components/StartButton.vue";
import { requests } from "@/util/requests";

export default {
  name: "IndexPage",
  props: {
    state: Object,
  },
  data() {
    return {
      playerName: "",
    };
  },
  methods: {
    startGame() {
      if (this.state.players.length === 0) {
        alert("You must select a board size and need at least one player in the lobby before starting the game!");
        return;
      }
      requests.start();
    },
    createBoard(dimensions) {
      requests.create(dimensions);
      this.$router.push({
        path: this.$route.path,
        query: {},
      });
    },
    confirmPlayerName(playerName) {
      if (playerName.trim() !== "") {
        requests.addPlayer(playerName);
        this.$router.push({
          path: this.$route.path,
          query: { ...this.$route.query, playerName: this.playerName },
        });
      }
    },
  },
  components: {
    GameDescription,
    RecommendedSizes,
    PlayerLobby,
    NameInput,
    StartButton,
  },
};
</script>
