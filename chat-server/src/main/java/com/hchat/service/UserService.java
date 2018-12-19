package com.hchat.service;

import com.hchat.pojo.TbUser;
import com.hchat.pojo.vo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p></p>
 *
 * @author xiaodongsun
 * @date 2018/12/19
 */
public interface UserService {

    /**
     * 查询所有用户
     */
    List<TbUser> findAll();

    /**
     * 用来登录检查，检查用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象
     */
    User login(String username, String password);

    /**
     * 注册用户保存到数据库
     * 抛出异常则注册失败
     * @param user 用户信息
     */
    void register(TbUser user);

    /**
     * 上传头像
     * @param file 客户端上传文件
     * @param userid 用户id
     * @return 如果上传成功返信息，否则抛出异常
     */
    User upload(MultipartFile file, String userid);

    /**
     * 更新用户的昵称
     * @param id 用户id
     * @param nickname 用户昵称
     */
    void updateNickname(String id, String nickname);

    /**
     * 根据用户id查找用户
     * @param userid 用户
     * @return 用户
     */
    User findById(String userid);

    /**
     * 根据用户名搜索用户(好友搜索)
     * @param userid 用户id
     * @param friendUsername 好友名称
     * @return 如果搜索到好友，就返回用户对象，否则返回空
     */
    User findByUsername(String userid, String friendUsername);
}
