package com.ruoyi.system.service.impl;

import cn.hutool.core.date.StopWatch;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.WordSnapshot;
import com.ruoyi.system.mapper.WordSnapshotMapper;
import com.ruoyi.system.service.IWordSnapshotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单词快照Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-24
 */
@Service
public class WordSnapshotServiceImpl implements IWordSnapshotService
{
    private static final Logger log = LoggerFactory.getLogger(WordSnapshotServiceImpl.class);

    @Autowired
    private WordSnapshotMapper wordSnapshotMapper;

    /**
     * 查询单词快照
     * 
     * @param id 单词快照主键
     * @return 单词快照
     */
    @Override
    public WordSnapshot selectWordSnapshotById(Long id)
    {
        return wordSnapshotMapper.selectWordSnapshotById(id);
    }

    /**
     * 查询单词快照列表
     * 
     * @param wordSnapshot 单词快照
     * @return 单词快照
     */
    @Override
    public List<WordSnapshot> selectWordSnapshotList(WordSnapshot wordSnapshot)
    {
        return wordSnapshotMapper.selectWordSnapshotList(wordSnapshot);
    }

    /**
     * 新增单词快照
     * 
     * @param wordSnapshot 单词快照
     * @return 结果
     */
    @Override
    public int insertWordSnapshot(WordSnapshot wordSnapshot)
    {
        wordSnapshot.setCreateTime(DateUtils.getNowDate());
        return wordSnapshotMapper.insertWordSnapshot(wordSnapshot);
    }

    /**
     * 修改单词快照
     * 
     * @param wordSnapshot 单词快照
     * @return 结果
     */
    @Override
    public int updateWordSnapshot(WordSnapshot wordSnapshot)
    {
        wordSnapshot.setUpdateTime(DateUtils.getNowDate());
        return wordSnapshotMapper.updateWordSnapshot(wordSnapshot);
    }

    /**
     * 批量删除单词快照
     * 
     * @param ids 需要删除的单词快照主键
     * @return 结果
     */
    @Override
    public int deleteWordSnapshotByIds(Long[] ids)
    {
        return wordSnapshotMapper.deleteWordSnapshotByIds(ids);
    }

    /**
     * 删除单词快照信息
     * 
     * @param id 单词快照主键
     * @return 结果
     */
    @Override
    public int deleteWordSnapshotById(Long id)
    {
        return wordSnapshotMapper.deleteWordSnapshotById(id);
    }

    @Override
    public int deleteByUserIdAndType(Long userId, int type) {
        return wordSnapshotMapper.deleteByUserIdAndType(userId, type);
    }

    @Override
    public int insertBatch(List<WordSnapshot> wordSnapshots) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int insertRows = wordSnapshotMapper.insertBatch(wordSnapshots);
        stopWatch.stop();
        log.info("批量插入条数:{} 耗时:{}ms", insertRows, stopWatch.getTotalTimeMillis());
        return insertRows;
    }
}
