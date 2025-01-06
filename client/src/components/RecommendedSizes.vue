<template>
  <section class="recommended-sizes">
    <h2 class="col-md-5">Recommended Board Sizes</h2>
    <div class="button-group mt-3">
      <button
          :class="['btn', selectedSize === 8 ? 'btn-blue' : 'btn-success']"
          @click="selectSize(8)"
      >
        8x8
      </button>
      <button
          :class="['btn', selectedSize === 10 ? 'btn-blue' : 'btn-success']"
          @click="selectSize(10)"
      >
        10x10
      </button>
      <button
          :class="['btn', selectedSize === 12 ? 'btn-blue' : 'btn-success']"
          @click="selectSize(12)"
      >
        12x12
      </button>
    </div>
    <!-- Warning message below the buttons -->
    <div
        v-show="![8, 10, 12].includes(selectedSize)"
        class="warning-message mt-3"
    >
      You must select a board size
    </div>
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
.recommended-sizes {
  text-align: center;
  margin-top: 20px;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.btn {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border-radius: 5px;
  border: none;
  transition: background-color 0.3s ease;
}

.btn-success {
  background-color: #198754;
  color: white;
}

.btn-success:hover {
  background-color: #0d5a37;
}

.btn-blue {
  background-color: #007bff;
  color: white;
}

.btn-blue:hover {
  background-color: #0056b3;
}

.warning-message {
  color: #ffdd8f;
  font-weight: bold;
  text-align: center;
  font-size: 14px;
}
</style>
