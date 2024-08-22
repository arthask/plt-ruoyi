<template>
  <div style="margin-top:10px">
    <el-row>
      <el-col v-if="showCard && this.packageType === 1" :span="18">
        <word-card
          :headerFront="headerFront"
          :footerFront="footerFront"
          :colorFront="colorFront"
          :colorTextFront="colorTextFront"
          :front="question"
          :back="answer">
        </word-card>
      </el-col>
      <el-col v-if="showCard && this.packageType === 3" :span="18">
        <ExpressionCard
          :back="answer"
          :colorFront="colorFront"
          :colorTextFront="colorTextFront"
          :footerFront="footerFront"
          :front="question"
          :headerFront="headerFront">
        </ExpressionCard>
      </el-col>
      <el-col :span="6">
        <el-select v-model="packageList" filterable placeholder="请选择闪卡集"
                   style="margin:20px;padding: 0 17px;"
                   @change="getCardOfPackage">
          <el-option
            v-for="item in options"
            :key="item.uuid"
            :label="item.name"
            :value="item.uuid">
          </el-option>
        </el-select>
        <div v-if="showCard">
          <el-statistic
            group-separator=","
            :value="totalCardCount"
            title="卡片总数"
          >
            <template slot="prefix">
              <i class="el-icon-s-flag" style="color: rgba(0, 123, 255, 0.72)"></i>
            </template>
          </el-statistic>
          <div v-for="item in tag" :key="item.name" class="tag" @click="searchClassifyCard(item.type)">
            {{ item.name }}
            <p>{{ item.count }}</p>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" type="flex" justify="left" v-if="showCard">
      <el-col :span="5">
      </el-col>
      <el-col :span="3">
        <el-button type="primary" plain @click="study(0)">不会</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="success" plain @click="study(2)">跳过</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="info" plain @click="study(1)">会</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import WordCard from './WordCard.vue'
import {getPackageList} from "@/api/bussiness/flashcardpackage";
import {getCardOfPackage, getClassifyCount, searchClassifyCard, studyCard} from "@/api/bussiness/flashcard";
import ExpressionCard from "./ExpressionCard.vue";
import {getPackageInfo} from "../../../../api/bussiness/flashcardpackage";

export default {
  components: {ExpressionCard, WordCard},
  data() {
    return {
      options: [],
      packageList: [],
      form: {
        flashcardId: [],
      },
      tag: [],
      colorFront: 'rgba(0, 123, 255, 0.72)',
      colorTextFront: 'white',
      question: 'hello this is a flashcard',
      answer: 'with animation',
      footerFront: "",
      headerFront: "",
      packageUUID: null,
      cardUUID: "",
      offset: 0,
      cardCount: 0,
      packageType: null,
      totalCardCount: null,
      type: null
    }
  },
  computed: {
    showCard: {
      get() {
        if (!this.packageUUID) {
          return false
        }
        return this.cardUUID;

      },
      cache: false,
    }
  },
  created() {
    getPackageList().then(res => {
      this.options = res.data;
    })
  },
  methods: {
    searchCardByType() {
      let params = {
        packageUUID: this.packageUUID,
        type: this.type,
        offset: this.offset
      }
      searchClassifyCard(params).then(res => {
        if (res.data && res.data.uuid) {
          this.question = res.data.front
          this.answer = res.data.back
          this.cardUUID = res.data.uuid
          this.cardCount = res.data.packageInfoDto.cardCount
        } else {
          this.$modal.msgWarning("没有这个类型的卡片了呢！");
        }
      })
    },
    getCard() {
      if (this.type) {
        this.searchCardByType();
      } else {
        let params = {
          packageUUID: this.packageUUID,
          offset: this.offset
        }
        getCardOfPackage(params).then(res => {
          this.cardUUID = ""
          if (res.data && res.data.uuid) {
            this.question = res.data.front
            this.answer = res.data.back
            this.cardUUID = res.data.uuid
            this.cardCount = res.data.packageInfoDto.cardCount
            if (!this.totalCardCount) {
              this.totalCardCount = this.cardCount;
            }
          }
        })
      }
      this.getCount()
    },
    getCardOfPackage(uuid) {
      this.reset()
      this.offset = 0;
      this.packageType = null;
      this.totalCardCount = null
      if (!uuid) {
        return;
      }
      this.packageUUID = uuid
      getPackageInfo(this.packageUUID).then(res => {
        this.packageType = res.data.type
        console.log(this.packageType)
      })
      this.getCard();
    },
    nextCard() {
      if (this.offset + 1 >= this.cardCount) {
        this.$modal.msgWarning("已经到最后了呢");
        return;
      }
      if (!this.packageUUID) {
        return;
      }
      this.offset++;
      this.getCard();
    },
    study(familiarity) {
      let params = {
        cardUUID: this.cardUUID,
        familiarity: familiarity
      }
      studyCard(params).then(res => {
        this.getCount();
      })
      this.nextCard();
    },
    getCount() {
      let params = {
        packageUUID: this.packageUUID,
      }
      getClassifyCount(params).then(res => {
        this.tag = []
        if (res.data && res.data.length > 0) {
          res.data.forEach((value) => {
            let item = {
              name: value.name,
              count: value.count,
              type: value.familiarity
            }
            this.tag.push(item)
          });
        }
      })
    },
    searchClassifyCard(type) {
      this.type = type
      this.offset = 0;
      this.searchCardByType()
    },
    reset() {
      this.question = null
      this.answer = null
    }
  }
}
</script>
<style scoped>
::v-deep .flashcard {
  height: 430px;
  //margin: 10px;
  //padding: 0px;
}

.tag {
  border-radius: 10px;
  margin: 20px;
  padding: 17px;
  background: rgb(214, 234, 249);
  width: 80%;
  height: 100px;
  text-align: center;
  color: rgba(0, 0, 0, 0.46);
}

.tag:hover {
  cursor: pointer; /* 鼠标样式变为手型 */
  transform: scale(1.1); /* 放大效果 */
  background: rgb(214, 234, 249);
}
</style>
