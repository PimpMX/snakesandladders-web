<template>
  <IndexPage v-if="state && !state.gameIsRunning" :state="state"/>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css';
import {connectWebSocket} from "@/util/websocket";
import {requests} from "@/util/requests";
import IndexPage from "@/components/IndexPage.vue";

export default {
  name: 'App',
  components: {IndexPage},
  data() {
    return {
      state: null
    }
  },
  async mounted() {

    this.state = await requests.state();

    console.log("Initial State");
    console.log(this.state);

    const onMessage = function (message) {
      this.state = message;
    }.bind(this);

    connectWebSocket(onMessage);
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
  background-color: #0e5932 !important;
  color: white !important;
  font-family: Arial, sans-serif;
}

h1 {
  font-size: 48px;
}

section {
  margin: 20px 0;
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
  margin: 20px 0;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}

</style>
