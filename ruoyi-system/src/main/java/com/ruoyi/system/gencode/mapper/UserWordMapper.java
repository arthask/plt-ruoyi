package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.gencode.entity.UserWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.gencode.entity.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户单词表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Mapper
public interface UserWordMapper extends BaseMapper<UserWord> {
    Word getNeedReviewWord(@Param("timeStr") String timeStr, @Param("userId") Long userId);
}
