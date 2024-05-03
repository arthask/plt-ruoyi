package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.LexiconData;
import com.ruoyi.system.domain.dto.LexiconShowData;
import com.ruoyi.system.gencode.entity.Label;
import com.ruoyi.system.gencode.entity.Lexicon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 词库表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
public interface LexiconService extends IService<Lexicon> {
    /**
     * 创建词库
     * @param lexiconData
     * @return
     */
    String createLexicon(LexiconData lexiconData);

    String updateLexicon(LexiconShowData lexiconShowData);

    String removeLexicon(Long[] ids);

    LexiconShowData getInfo(Long id);

    List<LexiconShowData> convertLexiconList(List<Lexicon> lexicons);

    List<Label> getLabelOfLexicon(String lexiconUUID);
}
