package com.hchat.service.impl;

import com.hchat.mapper.TbFriendMapper;
import com.hchat.mapper.TbFriendReqMapper;
import com.hchat.mapper.TbUserMapper;
import com.hchat.pojo.*;
import com.hchat.pojo.vo.User;
import com.hchat.service.UserService;
import com.hchat.utils.FastDFSClient;
import com.hchat.utils.IdWorker;
import com.hchat.utils.QRCodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TbUserMapper userMapper;

    @Autowired
    TbFriendMapper friendMapper;

    @Autowired
    TbFriendReqMapper friendReqMapper;

    @Autowired
    IdWorker idWorker;

    @Autowired
    FastDFSClient fastDFSClient;

    @Autowired
    QRCodeUtils qrCodeUtils;

    @Autowired
    Environment env;

    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    @Override
    public User login(String username, String password) {
        if (StringUtils.isNoneBlank(username) && StringUtils.isNoneBlank(password)) {
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();
            criteria.andUsernameEqualTo(username);
            List<TbUser> userList = userMapper.selectByExample(example);
            if (userList != null && userList.size() == 1){
                //密码校验
                String encodingPassword = DigestUtils.md5DigestAsHex(password.getBytes());
                if (encodingPassword.equals(userList.get(0).getPassword())){
                    User user = new User();
                    BeanUtils.copyProperties(userList.get(0), user);

                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public void register(TbUser user) {
        try {
            //1.判断用户名是否存在
            TbUserExample example = new TbUserExample();
            TbUserExample.Criteria criteria = example.createCriteria();
            criteria.andUsernameEqualTo(user.getUsername());
            List<TbUser> userList = userMapper.selectByExample(example);
            if (userList != null && userList.size() > 0){
                throw new RuntimeException("用户已存在");
            }

            //2.将用户存储到数据库
            //雪花算法生成唯一id
            user.setId(idWorker.nextId());
            user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
            user.setNickname(user.getUsername());
            //生成二维码，并将二维码保存到数据库中
            //生成二维码中的字符串
            String qrcodeStr = "hchat://" + user.getUsername();
            //临时目录
            String tmpdir = env.getProperty("hcat.tmpdir");
            String qrCodeFilePath = tmpdir + user.getUsername() + ".png";
            qrCodeUtils.createQRCode(qrCodeFilePath, qrcodeStr);

            String url = env.getProperty("fdfs.httpurl") + fastDFSClient.uploadFile(new File(qrCodeFilePath));
            user.setQrcode(url);

            user.setPicSmall(null);
            user.setPicNormal(null);
            user.setCreatetime(new Date());

            userMapper.insert(user);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("注册错误");
        }
    }

    @Override
    public User upload(MultipartFile file, String userid) {
        try {
            String url = fastDFSClient.uploadFile(file);
            //
            //
            String[] fileNameList = url.split("\\.");
            String fileName = fileNameList[0];
            String ext = fileNameList[1];

            String picSmallUrl = fileName + "_150x150." + ext;

            TbUser tbUser = userMapper.selectByPrimaryKey(userid);

            String prefix = env.getProperty("fdfs.httpurl");

            tbUser.setPicNormal(prefix + url);
            tbUser.setPicSmall(prefix + picSmallUrl);

            userMapper.updateByPrimaryKey(tbUser);
            //将用户信息返回controller
            User user = new User();
            BeanUtils.copyProperties(tbUser, user);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateNickname(String id, String nickname) {
        if (StringUtils.isNotBlank(nickname)){
            TbUser tbUser = userMapper.selectByPrimaryKey(id);
            tbUser.setNickname(nickname);
            userMapper.updateByPrimaryKey(tbUser);
        }else {
            throw new RuntimeException("昵称不能为空");
        }
    }

    @Override
    public User findById(String userid) {
        TbUser tbUser = userMapper.selectByPrimaryKey(userid);
        User user = new User();
        BeanUtils.copyProperties(tbUser, user);
        return user;
    }

    @Override
    public User findByUsername(String userid, String friendUsername) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(friendUsername);

        List<TbUser> userList = userMapper.selectByExample(example);
        TbUser friend = userList.get(0);
        //搜索用户不做校验
        //this.checkAllowToAddFriend(userid, friend);
        User friendUser = new User();
        BeanUtils.copyProperties(friend, friendUser);

        return friendUser;
    }

}
