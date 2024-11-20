<template>
  <div>
    <h1 class="text-muted">Hello</h1>
    <button class="btn btn-danger">test</button>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css';
import {connectWebSocket} from "@/util/websocket";
import {requests} from "@/util/requests";

let state = await requests.state();

console.log("Initial State");
console.log(state);

function onMessage(message) {
  console.log(message);
}

connectWebSocket(onMessage);

export default {
  name: 'App',
  components: {}
}
</script>

<style>
</style>
