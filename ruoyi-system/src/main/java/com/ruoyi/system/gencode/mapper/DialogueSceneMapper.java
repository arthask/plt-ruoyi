package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.domain.dto.scene.DialogueData;
import com.ruoyi.system.gencode.entity.DialogueScene;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 对话场景表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Mapper
public interface DialogueSceneMapper extends BaseMapper<DialogueScene> {
    /**
     * 获取场景下面的对话
     * @param sceneUUID 场景uuid
     * @return
     */
    List<DialogueData> getDialoguesOfScene(@Param("sceneUUID") String sceneUUID);
}
