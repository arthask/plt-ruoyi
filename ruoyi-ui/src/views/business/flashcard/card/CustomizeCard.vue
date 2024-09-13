<script>
export default {
  name: "CustomizeCard",
  props: {
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
    <div v-show="!isToggle" class="animated flipInX flashcard">
      <div>
        <div>
          <div class="ck-content">
            <div v-html="front"></div>
          </div>
        </div>
      </div>
      <div v-show="isToggle" class="animated flipInX flashcard">
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
  //text-align: center;
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
</style>
