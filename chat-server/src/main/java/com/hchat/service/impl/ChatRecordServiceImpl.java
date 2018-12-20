package com.hchat.service.impl;

import com.hchat.mapper.TbChatRecordMapper;
import com.hchat.pojo.TbChatRecord;
import com.hchat.pojo.TbChatRecordExample;
import com.hchat.service.ChatRecordService;
import com.hchat.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
@Service
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    TbChatRecordMapper chatRecordMapper;

    @Autowired
    IdWorker idWorker;

    @Override
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setHasRead(0);
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasDelete(0);

        chatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria1 = example.createCriteria();
        TbChatRecordExample.Criteria criteria2 = example.createCriteria();

        criteria1.andUseridEqualTo(userid);
        criteria2.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);

        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);

        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);

        example.or(criteria1);
        example.or(criteria2);

        //将发给userid的所有消息设置为已读
        TbChatRecordExample exampleQuerySendToMe = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteriaQuerySendToMe = exampleQuerySendToMe.createCriteria();

        criteriaQuerySendToMe.andFriendidEqualTo(userid);
        criteriaQuerySendToMe.andHasDeleteEqualTo(0);
        List<TbChatRecord> tbChatRecords = chatRecordMapper.selectByExample(exampleQuerySendToMe);
        for (TbChatRecord tbChatRecord:tbChatRecords){
            tbChatRecord.setHasRead(1);
            chatRecordMapper.updateByPrimaryKey(tbChatRecord);
        }


        return chatRecordMapper.selectByExample(example);
    }

    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = example.createCriteria();
        criteria.andFriendidEqualTo(userid);
        criteria.andHasReadEqualTo(0);
        return chatRecordMapper.selectByExample(example);
    }

    @Override
    public void updateStatusHasRead(String id) {
        TbChatRecord tbChatRecord = chatRecordMapper.selectByPrimaryKey(id);
        tbChatRecord.setHasRead(1);

        chatRecordMapper.updateByPrimaryKey(tbChatRecord);
    }
}
