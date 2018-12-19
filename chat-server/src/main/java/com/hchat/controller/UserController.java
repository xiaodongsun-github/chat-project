package com.hchat.controller;

import com.hchat.pojo.TbUser;
import com.hchat.pojo.vo.Result;
import com.hchat.pojo.vo.User;
import com.hchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/findAll")
    public List<TbUser> findAll(){
        return userService.findAll();
    }

    @PostMapping("/login")
    public Result login(@RequestBody TbUser user){
        try {
            User _user = null;
            _user = userService.login(user.getUsername(), user.getPassword());
            if (_user == null){
                return new Result(false, "登录失败，将检查用户名或者密码是否正确");
            }else {
                return new Result(true, "登录成功", _user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "登录错误");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody TbUser user){
        try {
            userService.register(user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }

    @PostMapping("/upload")
    public Result upload(MultipartFile file, String userid){
        try {
            User user = userService.upload(file, userid);
            if (user != null){
                return new Result(true, "上传成功", user);
            }else {
                return new Result(false, "上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传错误");
        }
    }

    @PostMapping("/updateNickname")
    public Result updateNickname(@RequestBody TbUser user){
        try {
            userService.updateNickname(user.getId(), user.getNickname());
            return new Result(true, "更新成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, "更新错误");
        }
    }

    @GetMapping("/findById")
    public User findById(String userid){
        return userService.findById(userid);
    }

    @GetMapping("/findByUsername")
    public Result findByUsername(String userid, String friendUsername){
        try {
            User user = userService.findByUsername(userid, friendUsername);
            if (user != null){
                return new Result(true, "搜索成功", user);
            }else {
                return new Result(true, "没有找到该用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "搜索错误");
        }
    }
}
