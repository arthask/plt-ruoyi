package com.example.pltool.service.language;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.language.word.WordShowData;
import com.example.pltool.domain.entity.UserWord;


/**
 * <p>
 * 用户单词表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
public interface UserWordService extends IService<UserWord> {
    /**
     * 收藏单词
     *
     * @param wordUUID 单词uuid
     * @param userId   用户id
     * @param userName 用户名
     */
    int collect(String wordUUID, Long userId, String userName);

    /**
     * 新增用户单词
     *
     * @param userWord 用户单词
     * @return 结果
     */
    int insertUserWord(UserWord userWord);


    WordShowData getNeedReviewWord(Long userId);
}
