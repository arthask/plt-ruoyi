package com.example.pltool.service.language;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.language.lexicon.LexiconData;
import com.example.pltool.domain.dto.language.lexicon.LexiconShowData;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.domain.entity.Lexicon;


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
