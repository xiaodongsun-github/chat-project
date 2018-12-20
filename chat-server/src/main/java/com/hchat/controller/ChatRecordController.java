package com.hchat.controller;

import com.hchat.pojo.TbChatRecord;
import com.hchat.service.ChatRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
@RestController
@RequestMapping("/chatrecord")
public class ChatRecordController {

    @Autowired
    ChatRecordService chatRecordService;

    @GetMapping("/findByUserIdAndFriendId")
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid){
        try {
            return chatRecordService.findByUserIdAndFriendId(userid, friendid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }

    @GetMapping("/findUnreadByUserid")
    public List<TbChatRecord> findUnreadByUserid(String userid){
        try {
            return chatRecordService.findUnreadByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<TbChatRecord>();
        }
    }
}
