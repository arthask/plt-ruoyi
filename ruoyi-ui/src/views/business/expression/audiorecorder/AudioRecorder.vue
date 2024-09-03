<script>
import RecordRTC from 'recordrtc';

export default {
  name: "AudioRecorder",
  data() {
    return {
      recorder: null,
      isRecording: false,
      audioUrl: null,
      stream: null,
      inputContent: ''
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
    },
    playContent() {
      this.speakCommon.speak(this.inputContent)
    },
    pausePlayContent() {
      this.speakCommon.pauseSpeak()
    },
    resumePlayContent() {
      this.speakCommon.resumeSpeak()
    }
  }
}
</script>

<template>
  <div>
    <el-row :gutter="20" justify="center" type="flex">
      <el-col :span="16">
        <div class="centered-div">
          <h3>练习内容</h3>
          <el-input
            v-model="inputContent"
            :rows="14"
            placeholder="请输入内容"
            resize="none"
            type="textarea">
          </el-input>
        </div>
        <div class="centered-div">
          <el-button plain type="success" @click="playContent">播放</el-button>
          <el-button plain type="warning" @click="pausePlayContent">暂停</el-button>
          <el-button plain type="info" @click="resumePlayContent">继续</el-button>
        </div>
        <el-divider></el-divider>
        <h3>成果检测</h3>
        <div class="centered-div">
          <el-button :disabled="isRecording" plain type="success" @click="startRecording">开始录音</el-button>
          <el-button :disabled="!isRecording" plain type="danger" @click="stopRecording">结束录音</el-button>
          <el-button :disabled="isRecording" plain type="primary" @click="restartRecording">重新开始</el-button>
        </div>
        <div>
          <audio v-if="audioUrl" :src="audioUrl" controls></audio>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" justify="center" type="flex">
      <el-col :span="8">

      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.centered-div {
  padding: 5px;
  align-content: center;
}
</style>
