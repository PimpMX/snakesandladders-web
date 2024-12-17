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
  let col = position;
  let row = 0;
  while(col > dim) {
    col -= dim;
    row++;
  }
  if(row % 2 === 1)
    col = dim - col;
  else
    col -= 1;
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
        const num = i * dim - j;
        const { x, y } = getFieldCoords(num, dim, sqSize, s);
        renderField(p, x, y, sqSize, num,
            j % 2 === 1 ? 'lightblue' : 'green', state);
      }
    } else {
      for(let j = dim; j > 0; j--) {
        const num = i * dim - j + 1;
        const { x, y } = getFieldCoords(num, dim, sqSize, s);
        renderField(p, x, y, sqSize, num,
            j % 2 === 0 ? 'lightblue' : 'green', state);
      }
    }
  }

  //renderSnakes(p, state, dim, sqSize, s);
  renderLadders(p, state, dim, sqSize, s);
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

/*
function renderSnakes(p, state, dim, sqSize, s) {
  for(const snake of state.board.snakes) {

    let { sX, sY } = getFieldCoords(snake.s, dim, sqSize, s);
    let { eX, eY } = getFieldCoords(snake.e, dim, sqSize, s);

    sX += s / 2;
    sY += s / 2;
    eX += s / 2;
    eY += s / 2;

    p.line(sX, sY, eX, eY);
  }
}*/

function renderLadders(p, state, dim, sqSize, s) {
  for(const snake of state.board.ladders) {

    let sp = getFieldCoords(snake.s, dim, sqSize, s);
    let ep = getFieldCoords(snake.e, dim, sqSize, s);

    sp.x += sqSize / 2;
    sp.y += sqSize / 2;
    ep.x += sqSize / 2;
    ep.y += sqSize / 2;

    p.push();
    p.stroke("orange");
    p.strokeWeight(5);
    p.line(sp.x - 10, sp.y, ep.x - 10, ep.y);
    p.line(sp.x + 10, sp.y, ep.x + 10, ep.y);
    p.pop();
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