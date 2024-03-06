package com.ruoyi.system.service;

import com.ruoyi.system.domain.WordSnapshot;

import java.util.List;

/**
 * 单词快照Service接口
 * 
 * @author ruoyi
 * @date 2024-01-24
 */
public interface IWordSnapshotService 
{
    /**
     * 查询单词快照
     * 
     * @param id 单词快照主键
     * @return 单词快照
     */
    public WordSnapshot selectWordSnapshotById(Long id);

    /**
     * 查询单词快照列表
     * 
     * @param wordSnapshot 单词快照
     * @return 单词快照集合
     */
    public List<WordSnapshot> selectWordSnapshotList(WordSnapshot wordSnapshot);

    /**
     * 新增单词快照
     * 
     * @param wordSnapshot 单词快照
     * @return 结果
     */
    public int insertWordSnapshot(WordSnapshot wordSnapshot);

    /**
     * 修改单词快照
     * 
     * @param wordSnapshot 单词快照
     * @return 结果
     */
    public int updateWordSnapshot(WordSnapshot wordSnapshot);

    /**
     * 批量删除单词快照
     * 
     * @param ids 需要删除的单词快照主键集合
     * @return 结果
     */
    public int deleteWordSnapshotByIds(Long[] ids);

    /**
     * 删除单词快照信息
     * 
     * @param id 单词快照主键
     * @return 结果
     */
    public int deleteWordSnapshotById(Long id);

    int deleteByUserIdAndType(Long userId, int type);

    int insertBatch(List<WordSnapshot> wordSnapshots);
}
