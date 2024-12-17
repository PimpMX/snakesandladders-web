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

  renderLadders(p, state, dim, sqSize, s);
  renderSnakes(p, state, dim, sqSize, s);
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

    p.push();
    p.fill(player.color);
    p.strokeWeight(3);
    p.circle(x + 0.5 * sqSize, y + 0.5 * sqSize, 0.5 * sqSize);
    p.pop();
  }
}

function vecSubtract(a, b) {
  return {
    x: b.x - a.x,
    y: b.y - a.y
  }
}

function vecAdd(a, b) {
  return {
    x: a.x + b.x,
    y: a.y + b.y
  }
}

function vecScale(vec, scale) {
  return {
    x: vec.x * scale,
    y: vec.y * scale
  }
}

function vecMagnitude(vec) {
  return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2));
}

function vecNormalize(vec) {
  const magnitude = vecMagnitude(vec);
  return {
    x: vec.x / magnitude,
    y: vec.y / magnitude
  }
}

function renderSnakes(p, state, dim, sqSize, s) {
  for(const snake of state.board.snakes) {

    let sp = getFieldCoords(snake.s, dim, sqSize, s);
    let ep = getFieldCoords(snake.e, dim, sqSize, s);

    sp.x += sqSize / 2;
    sp.y += sqSize / 2;
    ep.x += sqSize / 2;
    ep.y += sqSize / 2;

    const spEp = vecSubtract(sp, ep);
    const spEpMagnitude = vecMagnitude(spEp);
    const spEpNormalized = vecNormalize(spEp);
    const spEpNormal = {
      x: spEp.y,
      y: -spEp.x
    };
    const spEpNormalNormalized = vecNormalize(spEpNormal);

    // The two control points needed for drawing a bezier curve

    const c1 = vecAdd(
        vecAdd(sp, vecScale(spEpNormalized, spEpMagnitude * 0.4)),
        vecScale(spEpNormalNormalized, 40));

    const c2 = vecAdd(
        vecAdd(sp, vecScale(spEpNormalized, spEpMagnitude * 0.6)),
        vecScale(spEpNormalNormalized, -40));

    const eye1 = vecAdd(sp, vecScale(spEpNormalNormalized, 4));
    const eye2 = vecAdd(sp, vecScale(spEpNormalNormalized, -4));

    p.push();

    p.stroke("black");
    p.strokeWeight(24);
    p.bezier(sp.x, sp.y, c1.x, c1.y, c2.x, c2.y, ep.x, ep.y);

    p.stroke("green");
    p.strokeWeight(20);
    p.bezier(sp.x, sp.y, c1.x, c1.y, c2.x, c2.y, ep.x, ep.y);

    p.stroke("white");
    p.strokeWeight(6);
    p.point(eye1.x, eye1.y);
    p.point(eye2.x, eye2.y);

    p.pop();
  }
}

function renderLadders(p, state, dim, sqSize, s) {
  for(const snake of state.board.ladders) {

    let sp = getFieldCoords(snake.s, dim, sqSize, s);
    let ep = getFieldCoords(snake.e, dim, sqSize, s);

    // Center Start/End point at center origin of the square

    sp.x += sqSize / 2;
    sp.y += sqSize / 2;
    ep.x += sqSize / 2;
    ep.y += sqSize / 2;

    const spEp = vecSubtract(sp, ep);
    const spEpNormal = {
      x: spEp.y,
      y: -spEp.x
    };

    const spEpNormalNormalized = vecNormalize(spEpNormal);

    // Start/End points of first and second ladder leg

    const l1s = vecAdd(sp, vecScale(spEpNormalNormalized, -(sqSize / 6)));
    const l1e = vecAdd(ep, vecScale(spEpNormalNormalized, -(sqSize / 6)));
    const l2s = vecAdd(sp, vecScale(spEpNormalNormalized, sqSize / 6));
    const l2e = vecAdd(ep, vecScale(spEpNormalNormalized, sqSize / 6))

    // Direction vector of each leg

    const l1se = vecSubtract(l1s, l1e);
    const l1seNormalNormalized = vecNormalize({
      x: l1se.y,
      y: -l1se.x
    });

    for(let i = 1; i < Math.floor(vecMagnitude(l1se)) / (sqSize / 4); i++) {
      const curVec = vecAdd(l1s, vecScale(vecNormalize(l1se), i * (sqSize / 4)));
      const curVecRung = vecAdd(curVec, vecScale(l1seNormalNormalized, sqSize / 3));
      p.push();
      p.stroke("black")
      p.strokeWeight(8);
      p.line(curVec.x, curVec.y, curVecRung.x, curVecRung.y);
      p.stroke("orange");
      p.strokeWeight(5);
      p.line(curVec.x, curVec.y, curVecRung.x, curVecRung.y);
      p.pop();
    }

    p.push();
    p.stroke("black");
    p.strokeWeight(9);
    p.line(l1s.x, l1s.y, l1e.x, l1e.y);
    p.line(l2s.x, l2s.y, l2e.x, l2e.y);
    p.stroke("orange");
    p.strokeWeight(5);
    p.line(l1s.x, l1s.y, l1e.x, l1e.y);
    p.line(l2s.x, l2s.y, l2e.x, l2e.y);
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

.canvas-container >>> canvas {
  outline: 2px solid black;
}
</style>