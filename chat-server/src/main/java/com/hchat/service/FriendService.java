package com.hchat.service;

import com.hchat.pojo.vo.FriendReq;
import com.hchat.pojo.vo.User;

import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public interface FriendService {

    /**
     * 发送好友请求
     * @param fromUserid 申请好友的id
     * @param toUserid 要添加的好友的id
     */
    void sendRequest(String fromUserid, String toUserid);

    /**
     * 根据用户的id查询他对应的好友请求
     * @param userid 当前登录的用户id
     * @return 请求好友的用户列表
     */
    List<FriendReq> findFriendReqByUserid(String userid);

    /**
     * 接收好友请求
     * @param reqid 好友请求的id
     */
    void acceptFriendReq(String reqid);

    /**
     * 忽略好友请求
     * @param reqisd 好友请求的id
     */
    void ignoreFriendRqe(String reqisd);

    /**
     * 查询我的好友
     * @param userid 当前登录的用户id
     * @return 请求好友的用户列表
     */
    List<User> findFriendByUserid(String userid);
}
