package com.stars.ojbackenduserservice.controller.inner;


import com.stars.ojbackendservicclient.service.UserFeginClient;
import com.stars.ojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.stars.ojbackendmodel.model.entity.User;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 内部调用服务
 *
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeginClient {

    @Resource
    private UserService userService;

    /**
     * 获取用户id
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId")long userId){

        return userService.getById(userId);
    }


    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList){

        return userService.listByIds(idList);
    }
}
