package com.example.pltool.controller.business.label;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.service.LabelService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;

@RestController
@RequestMapping("/system/label")
public class LabelController extends BaseController {

  @Autowired
  private LabelService labelService;

  /**
   * 查询l列表
   */
  @GetMapping("/list")
  public TableDataInfo list(@RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "target", required = false) Integer type) {
    startPage();
    QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(name)) {
      queryWrapper.like("name", name);
    } ;
    if (Objects.nonNull(type)) {
      queryWrapper.eq("target", type);
    } ;
    queryWrapper.orderByDesc("create_time");
    List<Label> list = labelService.list(queryWrapper);
    List<LabelInfo> labelInfos = LabelInfo.convertListData2Vo(list);
    return getDataTable(labelInfos);
  }

  /**
   * 新增
   */
  @PostMapping("/add")
  public AjaxResult add(@RequestBody LabelInfo labelInfo) {
    labelInfo.setCreateUserId(getUserId());
    return AjaxResult.success(labelService.createLabel(labelInfo));
  }

  /**
   * 修改
   */
  @PostMapping("/update")
  public AjaxResult update(@RequestBody LabelInfo labelInfo) {
    return AjaxResult.success(labelService.updateLabel(labelInfo));
  }

  /**
   * 获取详情
   */
  @GetMapping("/getInfo")
  public AjaxResult getInfo(@RequestParam("uuid") String uuid) {
    return AjaxResult.success(labelService.getLabelInfo(uuid));
  }
}
