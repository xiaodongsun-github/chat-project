package com.hchat.service;

import com.hchat.pojo.TbChatRecord;

import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public interface ChatRecordService {

    /**
     * 将聊天记录保存到数据库
     * @param chatRecord 聊天记录
     */
    void insert(TbChatRecord chatRecord);

    /**
     * 根据用户id和好友id将聊天记录查询出来
     * @param userid 用户id
     * @param friendid 好友id
     * @return 聊天记录列表
     */
    List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid);

    /**
     * 根据用户id查询用户未读消息
     * @param userid 用户id
     * @return 未读消息列表
     */
    List<TbChatRecord> findUnreadByUserid(String userid);

    /**
     * 设置消息为已读
     * @param id 聊天记录id
     */
    void updateStatusHasRead(String id);
}
