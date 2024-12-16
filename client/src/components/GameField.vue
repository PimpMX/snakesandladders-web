<script>
import p5 from 'p5';
import {ref, onMounted, onBeforeUnmount} from 'vue';

export default {
  name: 'GameField',
  props: {
    state: Object
  },
  setup(props) {
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
        render(p, props.state);
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

function getFieldCoords(position, dim, sqSize, s) {
  const row = Math.floor(position / dim);
  let col = position;
  while(col > dim)
    col -= dim;
  console.log(position, row, col);
  if(row % 2 === 1)
    col = dim - col;
  return {
    x: col * sqSize,
    y: s - (row + 1) * sqSize
  }
}

const render = (p, state) => {

  if(!state)
    return;

  p.clear();

  const s = p.width;
  const dim = Math.sqrt(state.board.dimensions);
  const sqSize = s / dim;

  for(let i = 0; i <= dim; i++) {
    if(i % 2 === 0) {
      for(let j = 0; j < dim; j++) {
        const x = j * sqSize;
        const y = s - i * sqSize;
        const num = i * dim - j;
        renderField(p, x, y, sqSize, num,
            j % 2 === 1 ? 'lightblue' : 'green', state);
      }
    } else {
      for(let j = dim; j > 0; j--) {
        const x = s - j * sqSize;
        const y = s - i * sqSize;
        const num = i * dim - j + 1;
        renderField(p, x, y, sqSize, num,
            j % 2 === 0 ? 'lightblue' : 'green', state);
      }
    }
  }

  renderPlayers(p, state, dim, sqSize, s);
}

function renderField(p, x, y, size, fieldNum, bgColor) {
  p.fill(bgColor);
  p.square(x, y, size);

  p.fill('black');
  p.textSize(12);

  let txt = `${fieldNum}`;
  let txtWidth = p.textWidth(txt);
  p.text(txt, x + (size - txtWidth) / 2, y + size * 0.6);
}

function renderPlayers(p, state, dim, sqSize, s) {
  for(const player of state.players) {
    const { x, y } = getFieldCoords(player.position, dim, sqSize, s);
    p.fill(player.color);
    p.circle(x + 0.5 * sqSize, y + 0.5 * sqSize, 0.5 * sqSize);
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
}
</style>