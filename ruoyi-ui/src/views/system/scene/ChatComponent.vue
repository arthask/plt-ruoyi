<template>
  <div>
    <beautiful-chat
      :participants="participants"
      :titleImageUrl="titleImageUrl"
      :onMessageWasSent="onMessageWasSent"
      :messageList="messageList"
      :newMessagesCount="newMessagesCount"
      :isOpen="isChatOpen"
      :close="closeChat"
      :open="openChat"
      :showEmoji="false"
      :showFile="false"
      :showEdition="true"
      :showDeletion="true"
      :showTypingIndicator="showTypingIndicator"
      :showLauncher="false"
      :showCloseButton="true"
      :showHeader="true"
      :colors="colors"
      :disableUserListToggle="true"
      :alwaysScrollToBottom="alwaysScrollToBottom"
      :messageStyling="messageStyling"
      @onType="handleOnType"
      @edit="editMessage"/>
  </div>
</template>
<script>
import {getReplayInfo} from "@/api/conversation/scene";

export default {
  props: {
    sceneUuid: {
      type: String,
      default: ''
    },
  },
  watch: {
    sceneUuid: {
      handler(newUuid) {
        const query = {
          sceneUUId: newUuid,
          sortNum: this.newMessagesCount++
        };
        getReplayInfo(query).then(res => {
          this.buildReplyAndSpeak(res.msg);
        })
      }
    }
  },
  data() {
    return {
      participants: [
        {
          id: 'system',
          name: 'system',
          imageUrl: ''
        }
      ], // the list of all the participant of the conversation. `name` is the user name, `id` is used to establish the author of a message, `imageUrl` is supposed to be the user avatar.
      titleImageUrl: '',
      //https://a.slack-edge.com/66f9/img/avatars-teams/ava_0001-34.png
      messageList: [
        // { type: 'text', author: `system`, data: { text: `hi nice to meet you` } },
      ], // the list of the messages to show, can be paginated and adjusted dynamically
      newMessagesCount: 1,
      isChatOpen: true, // to determine whether the chat window should be open or closed
      showTypingIndicator: '', // when set to a value matching the participant.id it shows the typing indicator for the specific user
      colors: {
        header: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        launcher: {
          bg: '#4e8cff'
        },
        messageList: {
          bg: '#ffffff'
        },
        sentMessage: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        receivedMessage: {
          bg: '#eaeaea',
          text: '#222222'
        },
        userInput: {
          bg: '#f4f7f9',
          text: '#565867'
        }
      }, // specifies the color scheme for the component
      alwaysScrollToBottom: false, // when set to true always scrolls the chat to the bottom when new events are in (new message, user starts typing...)
      messageStyling: true // enables *bold* /emph/ _underline_ and such (more info at github.com/mattezza/msgdown)
    }
  },
  methods: {
    buildReplyAndSpeak(content) {
      const text = content;
      const reply = {author: 'system', type: 'text', data: {text}};
      this.messageList = [...this.messageList, reply]
      this.$emit("speak", text)
    },
    async onMessageWasSent(message) {
      // called when the user sends a message
      // 获取一条数据回复
      this.messageList = [...this.messageList, message]
      var query = {
        sceneUUId: this.sceneUuid,
        sortNum: this.newMessagesCount++
      }
      await getReplayInfo(query).then(res => {
        this.buildReplyAndSpeak(res.msg);
      })
    },
    openChat() {
      // called when the user clicks on the fab button to open the chat
      this.isChatOpen = true
      this.newMessagesCount = 1
    },
    closeChat() {
      // called when the user clicks on the botton to close the chat
      //this.isChatOpen = false
      this.newMessagesCount = 1
      this.$emit("closeConversation")
    },
    handleScrollToTop() {
      // called when the user scrolls message list to top
      // leverage pagination for loading another page of messages
    },
    handleOnType(word) {
      console.log(word)
    },
    editMessage(message) {
      const m = this.messageList.find(m => m.id === message.id);
      m.isEdited = true;
      m.data.text = message.data.text;
    },
    //语音播报
    speak(content) {
      this.speech.speak({text: content, queue: false,}).then(() => {
        console.log("播报完成...")
      })
    }
  }
}
</script>
