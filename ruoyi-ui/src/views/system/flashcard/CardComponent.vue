<template>
  <div>
    <info-card :frontType="'graph'"
               :frontTitle="front.title"
               :frontData="front.graphData"
               :backTitle="back.title"
               :backData="back.message" />
  </div>
</template>

<script>
import InfoCard from 'vue-info-card';

export default {
  components: {
    InfoCard,
  },
  data() {
    return {
      front: {
        title: 'Daily Conversion Value',
        graphData: [],
      },
      back: {
        title: 'Monthly Summary',
        message: 'Your average daily conversion value for this month is <b>50.4$</b>. It is below the average of the last six months.',
      },
    };
  },
};
</script>
