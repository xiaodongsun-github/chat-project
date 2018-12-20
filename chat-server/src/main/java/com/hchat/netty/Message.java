package com.hchat.netty;

import com.hchat.pojo.TbChatRecord;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class Message {

    /** 消息类型 */
    private Integer type;
    /** 聊天消息 */
    private TbChatRecord chatRecord;
    /** 扩展消息 */
    private Object ext;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
