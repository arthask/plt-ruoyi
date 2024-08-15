package com.example.pltool.domain.dto.label;

import com.example.pltool.domain.entity.Label;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class LabelInfo {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 标签类型
     */
    private Integer target;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 创建人id
     */
    private Long createUserId;

    public static List<LabelInfo> convertListData2Vo(List<Label> labelList) {
        if (CollectionUtils.isEmpty(labelList)) {
            return Collections.emptyList();
        }
        List<LabelInfo> result = new ArrayList<>();
        labelList.forEach(e -> {
            LabelInfo labelInfo = new LabelInfo();
            BeanUtils.copyProperties(e, labelInfo);
            result.add(labelInfo);
        });
        return result;
    }

    public static LabelInfo convertData2Vo(Label label) {
        if (Objects.isNull(label)) {
            return null;
        }
        LabelInfo labelInfo = new LabelInfo();
        BeanUtils.copyProperties(label, labelInfo);
        return labelInfo;
    }
}
