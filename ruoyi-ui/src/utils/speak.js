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
      voice: 'Google UK English Female',
      listeners: {
        'onvoiceschanged': (voices) => {
          console.log("Event voiceschanged", voices)
        }
      }
    }).then(() => {
    })
  },
  //语音播报
  speak(content) {
    this.speech.speak({text: content, queue: false,}).then(() => {
    })
  },
  changeVoice(language) {
    if (language === 'en-US') {
      this.speech.setVoice('Google US English')
    } else if (language === 'en-GB') {
      this.speech.setVoice('Google UK English Male')
    }
    this.speech.setLanguage(language)
  }
}
