package com.example.pltool.service.impl.language;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.entity.LexiconWord;
import com.example.pltool.mapper.LexiconWordMapper;
import com.example.pltool.service.language.LexiconWordService;

/**
 * <p>
 * 词库与单词关系表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Service
public class LexiconWordServiceImpl extends ServiceImpl<LexiconWordMapper, LexiconWord>
    implements LexiconWordService {

}
