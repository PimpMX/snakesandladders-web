<template>
  <section>
    <h2 class="col-md-4">Choose Your Name</h2>
    <div class="input-group justify-content-center mt-3">
      <input
          id="playerName"
          :value="playerName"
          @input="$emit('update:playerName', $event.target.value)"
          class="input-group-text col-md-2"
          @keydown.enter="confirmName"
          type="text"
          placeholder="Your Player Name"
          required
      />
      <button id="addPlayerButton" class="btn btn-success" @click="confirmName">Confirm</button>
    </div>
  </section>
</template>


<script>
export default {
  name: "NameInput",
  props: {
    playerName: String,
  },
  methods: {
    confirmName() {
      this.$emit("confirmPlayerName", this.playerName);
    },
  },
};

</script>
