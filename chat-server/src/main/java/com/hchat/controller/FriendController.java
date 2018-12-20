package com.hchat.controller;

import com.hchat.pojo.TbFriendReq;
import com.hchat.pojo.vo.FriendReq;
import com.hchat.pojo.vo.Result;
import com.hchat.pojo.vo.User;
import com.hchat.service.FriendService;
import org.apache.coyote.OutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @PostMapping("/sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq friendReq){
        try {
            friendService.sendRequest(friendReq.getFromUserid(), friendReq.getToUserid());
            return new Result(true, "已申请");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, "申请好友错误");
        }
    }

    @GetMapping("/findFriendReqByUserid")
    public List<FriendReq> findFriendReqByUserid(String userid){
        return friendService.findFriendReqByUserid(userid);
    }

    @PostMapping("/acceptFriendReq")
    public Result acceptFriendReq(String reqid){
        try {
            friendService.acceptFriendReq(reqid);
            return new Result(true, "添加好友成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加好友错误");
        }
    }

    @GetMapping("/ignoreFriendRqe")
    public Result ignoreFriendRqe(String reqisd){
        try {
            friendService.ignoreFriendRqe(reqisd);
            return new Result(true, "忽略好友请求成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "忽略好友请求错误");
        }
    }

    @GetMapping("findFriendByUserid")
    public List<User> findFriendByUserid(String userid){
        try {
            return friendService.findFriendByUserid(userid);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
}
