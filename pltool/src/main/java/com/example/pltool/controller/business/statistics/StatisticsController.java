package com.example.pltool.controller.business.statistics;

import com.example.pltool.service.IStatisticsService;
import com.example.pltool.domain.vo.StatisticsCountVo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计Controller
 *
 * @author ruoyi
 * @date 2023-12-28
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    @Autowired
    private IStatisticsService statisticsService;

    /**
     * 查询数量统计信息
     * @return AjaxResult
     */
    @GetMapping("/getStatisticsOfCount")
    public AjaxResult getStatisticsOfCount() {
        StatisticsCountVo statisticsOfCount = statisticsService.getStatisticsOfCount(this.getUserId());
        return AjaxResult.success(statisticsOfCount);
    }

    /**
     * 查询每一个月的学习记录数量
     * @return AjaxResult
     */
    @GetMapping("/getStudyRecordOfMonth")
    public AjaxResult getStudyRecordOfMonth() {
        return AjaxResult.success(statisticsService.getStudyRecordOfMonth(this.getUserId()));
    }

    /**
     * 查询每一个月学习的单词数量
     * @return AjaxResult
     */
    @GetMapping("/getUserWordOfMonth")
    public AjaxResult getUserWordOfMonth() {
        return AjaxResult.success(statisticsService.getUserWordOfMonth(this.getUserId()));
    }

    /**
     * 查询单词的阶段数量
     * @return AjaxResult
     */
    @GetMapping("/getUserWordPeriodCount")
    public AjaxResult getUserWordPeriodCount() {
        return AjaxResult.success(statisticsService.getUserWordPeriodCount(this.getUserId()));
    }

    /**
     * 查询一个月中每天的期望值与实际值
     * @return AjaxResult
     */
    @GetMapping("/getExceptAndActualValue")
    public AjaxResult getExceptAndActualValueOfDay() {
        return AjaxResult.success(statisticsService.getExceptAndActualValueOfDay(this.getUserId()));
    }

    /**
     * 查询用户总单词数，及未学习的新单词数
     * @return AjaxResult
     */
    @GetMapping("/getTotalAndNotStudyNum")
    public AjaxResult getTotalAndNotStudyNum() {
        return AjaxResult.success(statisticsService.getTotalAndNotStudyNum(this.getUserId()));
    }

    /**
     * 查询用户需复习总数、今日需复习单词总数、已复习单词数
     * @return AjaxResult
     */
    @GetMapping("/getNeedReviewAnHaveReviewNum")
    public AjaxResult getNeedReviewAnHaveReviewNum() {
        return AjaxResult.success(statisticsService.getNeedReviewAnHaveReviewNum(this.getUserId()));
    }

    /**
     * 查询用户总单词数，及未学习的新单词数
     * @return AjaxResult
     */
    @GetMapping("/getCollectionTotalAndNotStudyNum")
    public AjaxResult getCollectionTotalAndNotStudyNum(@RequestParam("wordCollectionId") String collectionUUId) {
        return AjaxResult.success(statisticsService.getCollectionTotalAndNotStudyNum(collectionUUId, this.getUserId()));
    }

}
