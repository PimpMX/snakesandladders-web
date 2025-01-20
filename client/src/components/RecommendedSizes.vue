<template>
  <section class="recommended-sizes">
    <h2 class="text-h6">Recommended Board Sizes</h2>
    <div class="button-group">
      <v-btn
          v-for="size in [8, 10, 12]"
          :key="size"
          :class="selectedSize === size ? 'selected-btn' : 'unselected-btn'"
          class="ma-2 custom-btn"
          @click="selectSize(size)"
      >
        {{ size }}x{{ size }}
      </v-btn>
    </div>
    <v-alert
        v-show="![8, 10, 12].includes(selectedSize)"
        type="warning"
        class="mt-3"
    >
      You must select a board size.
    </v-alert>
  </section>
</template>

<script>
export default {
  name: "RecommendedSizes",
  props: {
    selectedSize: Number,
  },
  methods: {
    selectSize(size) {
      this.$emit("createBoard", size);
    },
  },
};
</script>

<style scoped>
.custom-btn {
  font-size: 16px;
  font-weight: bold;
  padding: 12px 24px;
  border-radius: 8px;
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.unselected-btn {
  background-color: #198754 !important; /* Green */
  color: white !important;
}

.unselected-btn:hover {
  background-color: #146c43 !important; /* Darker green */
}

.selected-btn {
  background-color: #007bff !important; /* Blue */
  color: white !important;
}

.selected-btn:hover {
  background-color: #0056b3 !important; /* Darker blue */
}
</style>
