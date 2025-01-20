<template>

    <IndexPage v-if="state && !state.gameIsRunning" :state="state"/>
    <GamePage v-if="state && state.gameIsRunning" :state="state"/>
    <div class="offline-banner" v-if="this.offlineMode">Offline</div>
    <div class="d-flex align-items-center justify-content-between" style="position: absolute; top: 30px; left: 30px">
      <button
        v-if="!userCredentials"
        class="btn btn-primary me-2"
        @click="loginWithGoogle"
      >
        Login with Google
      </button>
      <button v-if="deferredPrompt"
        @click="installPwa"
        class="btn btn-success">
        Install App
      </button>
    </div>

    <img v-if="userCredentials?.photoURL"
       :src="`${userCredentials.photoURL}`"
       style="border-radius: 50%; position:absolute; top: 30px; right: 30px; height: 10%; width: auto"
       alt="Profile Image"
    />

</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css';
import { connectWebSocket } from "@/util/websocket";
import { requests } from "@/util/requests";
import IndexPage from "@/components/IndexPage.vue";
import GamePage from "@/components/GamePage.vue";
import {loginWithGoogle} from "@/firebase";

export default {
  name: 'App',
  components: { IndexPage, GamePage },
  data() {
    return {
      state: null,
      deferredPrompt: null,
      userCredentials: null,
      offlineMode: false
    }
  },
  async mounted() {
    this.state = await requests.state();
    this.offlineMode = window.isCacheResponse;
    const onMessage = function (message) {
      this.state = message;
    }.bind(this);
    connectWebSocket(onMessage);
    window.addEventListener('beforeinstallprompt', (event) => {
      event.preventDefault();
      this.deferredPrompt = event;
    });
    window.addEventListener("click", () => {
      this.offlineMode = window.isCacheResponse;
    })
  },
  methods: {
    async loginWithGoogle() {
      try {
        const user = await loginWithGoogle();
        console.log(`Welcome, ${user.displayName}`);
        this.userCredentials = user.toJSON();
        const firstName = user.displayName.split(" ")[0];
        this.$router.push({
          path: this.$route.path,
          query: { ...this.$route.query, playerName: firstName },
        });
        requests.addPlayer(firstName);
      } catch (error) {
        console.error("Error logging in:", error);
      }
    },
    installPwa() {
      if (this.deferredPrompt) {
        // Show the installation prompt
        this.deferredPrompt.prompt();
        this.deferredPrompt.userChoice.then((choiceResult) => {
          console.log(choiceResult.outcome); // Can be 'accepted' or 'dismissed'
          this.deferredPrompt = null; // Reset the prompt after it's used
        });
      }
    }
  }
}
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.controls {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
}

.controls button {
  background-color: #add8e6;
  color: black;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 10px;
  transition: background-color 0.3s ease;
}

.controls button:hover {
  background-color: #87ceeb;
}

body {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  margin: 0;
  background: radial-gradient(circle, rgba(0, 0, 0, 0.2) 0%, rgba(0, 0, 0, 0.7) 100%), #0e5932;
  background-size: cover;
  background-attachment: fixed;
  color: white !important;
  font-family: Arial, sans-serif;
}

h1 {
  font-size: 48px;
}

section {
  margin-top: 10px;
  margin-bottom: 10px;
  width: 100%;
}

section h2 {
  background-color: #003366;
  color: white;
  padding: 10px;
  border-radius: 0;
  width: 300px;
  margin: 0 auto 10px;
}

section.recommended-sizes {
  text-align: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

.install-pwa-button {
  position: absolute;
  top: 30px;
  left: 30px;
  background-color: #45a049;
}

.custom-btn {
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.offline-banner {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50px;
  background-color: #d32f2f;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  text-align: center;
  line-height: 50px;
  z-index: 1000;
  box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.2);
}

</style>
