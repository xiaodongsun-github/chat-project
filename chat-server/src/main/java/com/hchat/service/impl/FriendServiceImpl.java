package com.hchat.service.impl;

import com.hchat.mapper.TbFriendMapper;
import com.hchat.mapper.TbFriendReqMapper;
import com.hchat.mapper.TbUserMapper;
import com.hchat.pojo.*;
import com.hchat.pojo.vo.FriendReq;
import com.hchat.pojo.vo.User;
import com.hchat.service.FriendService;
import com.hchat.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    IdWorker idWorker;

    @Autowired
    TbFriendReqMapper friendReqMapper;

    @Autowired
    TbFriendMapper friendMapper;

    @Autowired
    TbUserMapper userMapper;

    @Override
    public void sendRequest(String fromUserid, String toUserid) {
        TbUser friend = userMapper.selectByPrimaryKey(toUserid);
        this.checkAllowToAddFriend(fromUserid, friend);
        //添加好友请求
        TbFriendReq friendReq = new TbFriendReq();
        friendReq.setId(idWorker.nextId());
        friendReq.setFromUserid(fromUserid);
        friendReq.setToUserid(toUserid);
        friendReq.setCreatetime(new Date());
        friendReq.setStatus(0);

        friendReqMapper.insert(friendReq);
    }

    @Override
    public List<FriendReq> findFriendReqByUserid(String userid) {
        //根据用户的id查询对应的好友请求
        TbFriendReqExample example = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria = example.createCriteria();
        criteria.andToUseridEqualTo(userid);
        criteria.andStatusEqualTo(0);
        List<TbFriendReq> friendReqList = friendReqMapper.selectByExample(example);

        List<FriendReq> friendUserList = new ArrayList<>();
        //根据好友请求，将发起好友请求的用户信息的返回
        for (TbFriendReq tbFriendReq:friendReqList){
            TbUser tbUser = userMapper.selectByPrimaryKey(tbFriendReq.getFromUserid());
            FriendReq friendReq = new FriendReq();
            BeanUtils.copyProperties(tbUser, friendReq);
            //设置好友的请求id
            friendReq.setId(tbFriendReq.getId());
            friendUserList.add(friendReq);
        }
        return friendUserList;
    }

    @Override
    @Transactional
    public void acceptFriendReq(String reqid) {
        TbFriendReq friendReq = friendReqMapper.selectByPrimaryKey(reqid);
        friendReq.setStatus(1);
        friendReqMapper.updateByPrimaryKey(friendReq);

        TbFriend friend = new TbFriend();
        friend.setId(idWorker.nextId());
        friend.setUserid(friendReq.getFromUserid());
        friend.setFriendsId(friendReq.getToUserid());
        friend.setCreatetime(new Date());

        TbFriend friend1 = new TbFriend();
        friend1.setId(idWorker.nextId());
        friend1.setUserid(friendReq.getToUserid());
        friend1.setFriendsId(friendReq.getFromUserid());
        friend1.setCreatetime(new Date());

        friendMapper.insert(friend);
        friendMapper.insert(friend1);
    }

    @Override
    public void ignoreFriendRqe(String reqisd) {
        TbFriendReq friendReq = friendReqMapper.selectByPrimaryKey(reqisd);
        friendReq.setStatus(1);
        friendReqMapper.updateByPrimaryKey(friendReq);
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria criteria = friendExample.createCriteria();
        criteria.andUseridEqualTo(userid);
        List<TbFriend> tbFriendList = friendMapper.selectByExample(friendExample);

        List<User> friendList = new ArrayList<>();
         for (TbFriend tbFriend:tbFriendList){
             TbUser tbUser = userMapper.selectByPrimaryKey(tbFriend.getFriendsId());
             User friend = new User();
             BeanUtils.copyProperties(tbUser, friend);
             friendList.add(friend);
         }

        return friendList;
    }

    /**
     * 检查是否允许添加好友
     * @param userid
     * @param friend
     */
    private void checkAllowToAddFriend(String userid, TbUser friend){
        if (friend.getId().equals(userid)){
            throw new RuntimeException("不能添加自己为好友");
        }

        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria criteria1 = friendExample.createCriteria();
        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendsIdEqualTo(friend.getId());

        List<TbFriend> friendList = friendMapper.selectByExample(friendExample);

        if (friendList != null && friendList.size() > 0){
            throw new RuntimeException(friend.getUsername() + "已经是好友了");
        }

        TbFriendReqExample friendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria criteria2 = friendReqExample.createCriteria();
        criteria2.andFromUseridEqualTo(userid);
        criteria2.andToUseridEqualTo(friend.getId());
        criteria2.andStatusEqualTo(0);
        List<TbFriendReq> friendReqList = friendReqMapper.selectByExample(friendReqExample);
        if (friendReqList != null && friendReqList.size() > 0){
            throw new RuntimeException("已经申请了");
        }
    }

}
