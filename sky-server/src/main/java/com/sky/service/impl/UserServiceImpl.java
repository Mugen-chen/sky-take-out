package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    // 微信接口服务地址
    private static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 根据微信授权码实现微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 首先调用微信接口服务，获取openid
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断openid是否为空，空值则登陆失败，抛出业务异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断是否为新用户
        User user = userMapper.getByOpenid(openid);

        // 如果是新用户，自动完成注册，将openid插入到user表
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }
        // 返回用户对象
        return user;
    }

    /**
     * 调用微信接口服务，获取openid
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("appid",weChatProperties.getAppid());
        paraMap.put("secret",weChatProperties.getSecret());
        paraMap.put("js_code",code);
        paraMap.put("grant_type","authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, paraMap);
        log.info("微信登录返回结果：{}", json);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        log.info("微信用户的openid为：{}", openid);

        return openid;
    }
}
