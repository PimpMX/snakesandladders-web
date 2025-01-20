<template>
  <div class="container">
    <GameDescription/>
    <RecommendedSizes
        :selectedSize="Math.sqrt(state.board.dimensions)"
        @createBoard="createBoard"
    />
    <PlayerLobby :players="state.players" :local-player-name="playerName"/>
    <div v-if="!$route.query.playerName">
      <button
          v-if="!isLoggedIn"
          class="btn btn-primary mt-3"
          @click="loginWithGoogle"
      >
        Login with Google
      </button>
    </div>
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
import { loginWithGoogle } from "@/firebase";


export default {
  name: "IndexPage",
  props: {
    state: Object,
  },
  data() {
    return {
      playerName: "",
      isLoggedIn: false, // Tracks if the user is logged in
    };
  },
  methods: {
    async loginWithGoogle() {
      try {
        const user = await loginWithGoogle();
        console.log(`Welcome, ${user.displayName}`);
        this.isLoggedIn = true;

        // Extract the first name by splitting the display name on spaces
        const firstName = user.displayName.split(" ")[0];
        this.playerName = firstName; // Set the player name to the first name
        alert(`Logged in as ${firstName}. You can edit your name if needed.`);
      } catch (error) {
        console.error("Error logging in:", error);
      }
    },
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
<style scoped>
.btn {
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
}

.btn-primary {
  background-color: #4285f4;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #357ae8;
}

.mt-3 {
  margin-top: 20px;
}
</style>
