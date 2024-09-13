<template>
  <div style="margin-top:10px">
    <el-row :gutter="20">
      <el-col v-if="showCard" :span="18">
        <el-carousel ref="carousel" :autoplay="false" :loop="false"
                     height="500px" indicator-position="none">
          <el-carousel-item v-for="item in viewCardList" :key="item.uuid">
            <ExpressionCard
              v-if="packageType === 3"
              :back="item.back"
              :colorFront="colorFront"
              :colorTextFront="colorTextFront"
              :footerFront="footerFront"
              :front="item.front"
              :headerFront="headerFront"
              :is-toggle="showBack"
            >
            </ExpressionCard>
            <word-card
              v-if="packageType === 1"
              :back="item.back"
              :colorFront="colorFront"
              :colorTextFront="colorTextFront"
              :footerFront="footerFront"
              :front="item.front"
              :headerFront="headerFront"
              :is-toggle="showBack"
            >
            </word-card>
            <CustomizeCard
              v-if="packageType === 4"
              :back="item.back"
              :front="item.front"
              :is-toggle="showBack">
            </CustomizeCard>
          </el-carousel-item>
        </el-carousel>
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
        <div v-if="showCard" style="margin-top: 30px">
          <el-row :gutter="20">
            <el-col :span="8">
              <div>
                <el-statistic
                  :value="totalCardCount"
                  group-separator=","
                  title="卡片总数"
                  :value-style="{ fontWeight: 'bold' }"
                >
                  <template slot="prefix">
                    <i class="el-icon-s-flag" style="color: rgba(0, 123, 255, 0.72)"></i>
                  </template>
                </el-statistic>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-statistic
                  :value="okCardCount"
                  group-separator=","
                  title="会卡片数"
                  :value-style="{ color: 'green', fontWeight: 'bold'}"
                >
                </el-statistic>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="statistic" @click="studyNoRightCard">
                <el-statistic
                  :value="noRightCardCount"
                  group-separator=","
                  title="不会卡片数"
                  :value-style="{ color: 'red',fontWeight: 'bold' }"
                >
                </el-statistic>
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-if="showCard" style="margin-top: 50px">
          <el-row :gutter="20" justify="center" type="flex">
            <el-col :span="12">
              <el-progress :color="colors" :percentage="percentage"
                           :stroke-width="12" type="dashboard">
              </el-progress>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-row v-if="showCard" :gutter="20" justify="center" type="flex">
      <el-col v-if="!showOption && !showRestart" :span="9">
        <el-button plain style="width: 150px;height: 50px" type="primary" @click="showCardOption">查看答案</el-button>
      </el-col>
      <el-col v-if="showRestart" :span="9">
        <el-button plain style="width: 150px;height: 50px" type="primary" @click="restart">重新开始</el-button>
      </el-col>
      <el-col v-if="showOption" :span="9">
        <el-button plain style="width: 150px;height: 50px" type="primary" @click="study(0)">会</el-button>
      </el-col>
      <el-col v-if="showOption" :span="9">
        <el-button plain style="width: 150px;height: 50px" type="danger" @click="study(1)">不会</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import WordCard from './WordCard.vue'
import {getPackageInfo, getPackageList} from "@/api/bussiness/flashcardpackage";
import {getCardListInPackage, getClassifyCount, searchClassifyCard} from "@/api/bussiness/flashcard";
import ExpressionCard from "./ExpressionCard.vue";
import CustomizeCard from "./CustomizeCard.vue";

export default {
  components: {CustomizeCard, ExpressionCard, WordCard},
  name: "CardIndex",
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
      type: null,
      cardList: [],
      showOption: false,
      showBack: false,
      showRestart: false,
      okCardCount: 0,
      noRightCardCount: 0,
      okCardList: [],
      noRightCardList: [],
      viewCardList: [],
      percentage: 0,
      colors: [
        {color: '#f56c6c', percentage: 20},
        {color: '#e6a23c', percentage: 40},
        {color: '#5cb87a', percentage: 60},
        {color: '#1989fa', percentage: 80},
        {color: '#6f7ad3', percentage: 100}
      ]
    }
  },
  computed: {
    showCard: {
      get() {
        if (!this.packageUUID) {
          return false
        }
        return this.cardList.length > 0;

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
        } else {
          this.$modal.msgWarning("没有这个类型的卡片了呢！");
        }
      })
    },
    getCardOfPackage(uuid) {
      this.reset()
      this.offset = 0
      this.okCardCount = 0
      this.noRightCardCount = 0
      this.showRestart = false
      this.packageType = null;
      this.totalCardCount = null
      this.noRightCardList = []
      this.percentage = 0
      if (!uuid) {
        return;
      }
      this.packageUUID = uuid
      getPackageInfo(this.packageUUID).then(res => {
        this.packageType = res.data.type
        console.log(this.packageType)
      })
      let params = {
        packageUUID: this.packageUUID
      }
      getCardListInPackage(params).then(res => {
        this.cardList = res.data
        this.viewCardList = [...this.cardList]
        this.totalCardCount = this.viewCardList.length
      })
    },
    nextCard() {
      this.nextSlide()
      if (!this.packageUUID) {
        return;
      }
      this.percentage = Math.floor((this.offset + 1) / this.totalCardCount * 100);
      if (this.percentage > 100) {
        this.percentage = 100;
      }
      this.offset++;
    },
    study(familiarity) {
      this.showOption = false
      const currentCardInfo = this.viewCardList[this.getCurrentIndex()];
      if (familiarity === 0) {
        this.okCardCount++
        this.okCardList.push(currentCardInfo)
      } else if (familiarity === 1) {
        this.noRightCardCount++
        this.noRightCardList.push(currentCardInfo)
      }
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
      this.showBack = false
      this.showOption = false
    },
    goToSlide(index) {
      this.$refs.carousel.setActiveItem(index);
      this.showBack = false
    },
    nextSlide() {
      if (this.offset > this.totalCardCount - 1) {
        return;
      }
      if (this.offset === this.totalCardCount - 1) {
        this.showRestart = true
        this.showOption = false
        return
      }
      const currentIndex = this.getCurrentIndex()
      const nextIndex = (currentIndex + 1) % this.totalCardCount;
      this.goToSlide(nextIndex);
    },
    showCardOption() {
      this.showOption = true
      this.showBack = true
    },
    restart() {
      this.offset = 0
      this.okCardCount = 0
      this.noRightCardCount = 0
      this.showBack = false
      this.showRestart = false
      this.percentage = 0
      this.goToSlide(0)
    },
    getCurrentIndex() {
      const carousel = this.$refs.carousel;
      return carousel.activeIndex;
    },
    studyNoRightCard() {
      this.offset = 0
      this.showBack = false
      this.showRestart = false
      this.totalCardCount = this.noRightCardCount
      this.okCardCount = 0
      this.noRightCardCount = 0
      this.viewCardList = [...this.noRightCardList]
      this.noRightCardList = []
      this.percentage = 0
      this.goToSlide(0)
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

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.statistic {
  cursor: pointer;
}
</style>
