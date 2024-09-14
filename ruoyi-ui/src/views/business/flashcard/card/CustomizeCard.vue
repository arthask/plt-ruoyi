<script>
export default {
  name: "CustomizeCard",
  props: {
    textSizeFront: {
      type: String,
      default: '2em'
    },
    textSizeBack: {
      type: String,
      default: '2em'
    },
    colorTextFront: {
      type: String,
      default: 'black'
    },
    colorTextBack: {
      type: String,
      default: 'black'
    },
    colorFront: {
      type: String,
      default: 'white'
    },
    colorBack: {
      type: String,
      default: '#2ecc71'
    },
    front: {
      type: String,
      default: ''
    },
    back: {
      type: String,
      default: ''
    },
    isToggle: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {}
  }
}
</script>

<template>
  <div>
    <div v-show="!isToggle" class="animated flipInX flashcard scroll-container"
         v-bind:style="{fontSize: textSizeFront,fontWeight: 'bold', color: colorTextFront}">
      <div style="width: 70%;margin: 0 auto;">
        <div class="ck-content">
            <div v-html="front"></div>
          </div>
        </div>
      </div>
    <div v-show="isToggle" class="animated flipInX flashcard scroll-container"
         v-bind:style="{fontSize: textSizeBack,fontWeight: 'bold', color: colorTextBack}">
      <div style="width: 70%;margin: 0 auto;">
        <div class="ck-content">
          <div v-html="back"></div>
        </div>
        </div>
      </div>
    </div>
</template>

<style lang="scss" scoped>
.center {
  text-align: center;
}

.flashcard {
  cursor: pointer;
  border-radius: 10px;
  margin: 20px;
  padding: 25px;
  box-shadow: 0 0px 10px rgba(0, 0, 0, 0.4);
  text-align: center;
}

.flashcard:hover {
  box-shadow: 0 0px 25px rgba(0, 0, 0, 0.8);
}

.animated {
  animation-duration: 1s;
  animation-fill-mode: both;
}

@keyframes flipInX {
  from {
    transform: perspective(400px) rotate3d(1, 0, 0, 90deg);
    animation-timing-function: ease-in;
    opacity: 0;
  }
  40% {
    transform: perspective(400px) rotate3d(1, 0, 0, -20deg);
    animation-timing-function: ease-in;
  }
  60% {
    transform: perspective(400px) rotate3d(1, 0, 0, 10deg);
    opacity: 1;
  }
  80% {
    transform: perspective(400px) rotate3d(1, 0, 0, -5deg);
  }
  to {
    transform: perspective(400px);
  }
}

.flipInX {
  backface-visibility: visible !important;
  animation-name: flipInX;
}

.scroll-container {
  height: 100%; /* 固定高度 */
  overflow-y: auto; /* 自动显示纵向滚动条 */
}

// 用于控制图片展示大小
::v-deep .ck-content .image img {
  height: 30%;
  max-width: 100%;
  min-width: 100%;
}
</style>
