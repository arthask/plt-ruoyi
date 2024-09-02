<script>
import RecordRTC from 'recordrtc';

export default {
  name: "AudioRecorder",
  data() {
    return {
      recorder: null,
      isRecording: false,
      audioUrl: null,
      stream: null
    };
  },
  methods: {
    async startRecording() {
      try {
        this.stream = await navigator.mediaDevices.getUserMedia({audio: true});
        this.recorder = new RecordRTC(this.stream, {
          type: 'audio',
          mimeType: 'audio/wav'
        });
        this.recorder.startRecording();
        this.isRecording = true;
      } catch (error) {
        console.error('Error accessing the microphone', error);
      }
    },
    stopRecording() {
      if (this.recorder) {
        this.recorder.stopRecording(() => {
          const audioBlob = this.recorder.getBlob();
          this.audioUrl = URL.createObjectURL(audioBlob);
          // Stop the stream to release the microphone
          this.stream.getTracks().forEach(track => track.stop());
        });
        this.isRecording = false;
      }
    },
    restartRecording() {
      if (this.isRecording) {
        this.stopRecording(); // Stop current recording if it's in progress
      }
      // Reset audio URL and other states
      this.audioUrl = null;
      this.startRecording(); // Start a new recording session
    }
  }
}
</script>

<template>
  <el-row :gutter="20" justify="center" type="flex">
    <el-col :span="8">
      <div class="centered-div">
        <el-button :disabled="isRecording" type="success" @click="startRecording">开始录音</el-button>
        <el-button :disabled="!isRecording" type="danger" @click="stopRecording">结束录音</el-button>
        <el-button :disabled="isRecording" type="primary" @click="restartRecording">重新开始</el-button>
      </div>
      <div>
        <audio v-if="audioUrl" :src="audioUrl" controls></audio>
      </div>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.centered-div {
  padding: 20px;
}
</style>
