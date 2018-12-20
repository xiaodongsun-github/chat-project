package com.hchat.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>建立用户和通道的关联</p>
 *
 * @author xiaodongsun
 * @date 2018/12/20
 */
public class UserChannelMap {

    /** 保存用户和通道的map */
    private static Map<String, Channel> userChannelMap;

    static {
        userChannelMap = new HashMap<>();
    }

    /**
     * 添加用户id与channel的关联
     * @param userid
     * @param channel
     */
    public static void put(String userid, Channel channel){
        userChannelMap.put(userid, channel);
    }

    /**
     * 移除用户id与channel的关联
     * @param userid
     */
    public static void remove(String userid){
        userChannelMap.remove(userid);
    }

    /**
     * 根据通道id移除用户与channel的关联
     * @param channelId
     */
    public static void removeByChannelId(String channelId){
        if (!StringUtils.isNotBlank(channelId)) {
            return;
        }
        for (String s : userChannelMap.keySet()){
            Channel channel = userChannelMap.get(s);
            if (channelId.equals(channel.id().asLongText())){
                System.out.println("客户端连接断开，取消用户" + s + "与通道" + channelId + "的关联");
                userChannelMap.remove(s);
                break;
            }
        }
    }

    public static void print(){
        for (String s:userChannelMap.keySet()){
            System.out.println("用户id： " + s + " 通道: " + userChannelMap.get(s).id());
        }
    }

    /**
     * 根据好友id获取对应的通道
     * @param friendid 好友id
     * @return netty通道
     */
    public static Channel get(String friendid) {
        return userChannelMap.get(friendid);
    }
}
