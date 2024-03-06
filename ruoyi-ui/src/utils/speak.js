import Speech from "speak-tts";

export default {
  speechInit() {
    this.speech = new Speech();
    this.speech.init({
      volume: 1,
      // en-GB、zh-CN
      lang: "en-GB",
      rate: 1,
      pitch: 1,
    }).then(() => {
      console.log('语音播报初始化完成...')
    })
  },
  //语音播报
  speak(content) {
    this.speech.speak({text: content, queue: false,}).then(() => {
      console.log("播报完成...")
    })
  }
}
