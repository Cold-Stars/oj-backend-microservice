package com.stars.ojbackendservicclient.service;

import com.stars.ojbackendcommon.common.ErrorCode;
import com.stars.ojbackendcommon.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.stars.ojbackendmodel.model.entity.User;
import com.stars.ojbackendmodel.model.enums.UserRoleEnum;
import com.stars.ojbackendmodel.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

import static com.stars.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 *
 * @author ColdStars</a>

 */

@FeignClient(name = "oj-backend-user-service",path = "/api/user/inner")
public interface UserFeginClient {


    /**
     * 获取用户id
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("userId") long userId);


    @GetMapping("/get/ids")
    List<User> listByIds(@RequestParam("idList")Collection<Long> idList);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }
    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    default  boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */

    default UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
