<template>
  <div style="margin-top:10px">
    <el-row>
      <el-col v-if="showCard" :span="18">
        <el-carousel ref="carousel" :autoplay="false" :loop="false" height="500px" indicator-position="none">
          <el-carousel-item v-for="item in cardList" :key="item.uuid">
            <ExpressionCard
              v-if="packageType === 3"
              :back="item.back"
              :colorFront="colorFront"
              :colorTextFront="colorTextFront"
              :footerFront="footerFront"
              :front="item.front"
              :headerFront="headerFront">
            </ExpressionCard>
            <word-card
              v-if="packageType === 1"
              :back="item.back"
              :colorFront="colorFront"
              :colorTextFront="colorTextFront"
              :footerFront="footerFront"
              :front="item.front"
              :headerFront="headerFront">
            </word-card>
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
      <!--      <el-col :span="5">-->
      <!--      </el-col>-->
      <!--      <el-col :span="3">-->
      <!--        <el-button type="primary" plain @click="study(0)">不会</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="3">-->
      <!--        <el-button type="success" plain @click="study(2)">跳过</el-button>-->
      <!--      </el-col>-->
      <!--      <el-col :span="3">-->
      <!--        <el-button type="info" plain @click="study(1)">会</el-button>-->
      <!--      </el-col>-->
    </el-row>
  </div>
</template>
<script>
import WordCard from './WordCard.vue'
import {getPackageList} from "@/api/bussiness/flashcardpackage";
import {getClassifyCount, searchClassifyCard, studyCard} from "@/api/bussiness/flashcard";
import ExpressionCard from "./ExpressionCard.vue";
import {getPackageInfo} from "../../../../api/bussiness/flashcardpackage";
import {getCardListInPackage} from "../../../../api/bussiness/flashcard";

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
      type: null,
      cardList: []
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
          this.cardCount = res.data.packageInfoDto.cardCount
        } else {
          this.$modal.msgWarning("没有这个类型的卡片了呢！");
        }
      })
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
      let params = {
        packageUUID: this.packageUUID
      }
      getCardListInPackage(params).then(res => {
        this.cardList = res.data
        this.totalCardCount = this.cardList.length
      })
    },
    nextCard() {
      this.nextSlide()
      console.log("nextCard")
      if (this.offset + 1 >= this.cardCount) {
        this.$modal.msgWarning("已经到最后了呢");
        return;
      }
      if (!this.packageUUID) {
        return;
      }
      this.offset++;
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
    },
    goToSlide(index) {
      this.$refs.carousel.setActiveItem(index);
    },
    nextSlide() {
      const carousel = this.$refs.carousel;
      const currentIndex = carousel.activeIndex;
      if (currentIndex === this.totalCardCount - 1) {
        return
      }
      const nextIndex = (currentIndex + 1) % this.totalCardCount;
      this.goToSlide(nextIndex);
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
</style>
