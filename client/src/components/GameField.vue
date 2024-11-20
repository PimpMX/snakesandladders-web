<script>
import p5 from 'p5';
import {ref, onMounted, onBeforeUnmount} from 'vue';

export default {
  name: 'GameField',
  setup() {
    const canvasContainer = ref(null);
    let p5Instance = null;
    const updateCanvasSize = (canvasContainer, p) => {
      const containerWidth = canvasContainer.value.offsetWidth;
      const containerHeight = canvasContainer.value.offsetHeight;
      p.resizeCanvas(containerWidth, containerHeight);
      p.background(255);
    };
    const gameFieldCanvas = (p) => {
      p.setup = () => {
        const containerWidth = canvasContainer.value.offsetWidth;
        const containerHeight = canvasContainer.value.offsetHeight;
        p.createCanvas(containerWidth, containerHeight).parent(canvasContainer.value);
        p.background(255);
      };
      p.draw = () => {
        renderField(p);
      };
      p.windowResized = () => {
        updateCanvasSize(canvasContainer, p);
      }
    };
    onMounted(() => {
      if (!p5Instance) {
        p5Instance = new p5(gameFieldCanvas);
      }
    });
    onBeforeUnmount(() => {
      if (p5Instance) {
        p5Instance.remove();
      }
    });
    return {
      canvasContainer,
    };
  },
};

const renderField = (p) => {
  if (p.mouseIsPressed) {
    p.fill(50);
    p.noStroke();
    p.ellipse(p.mouseX, p.mouseY, 20, 20);
  }
}
</script>

<template>
  <div ref="canvasContainer" class="canvas-container"></div>
</template>

<style scoped>
.canvas-container {
  height: 35vw;
  width: 35vw;
  border: 1px solid black;
}
</style>