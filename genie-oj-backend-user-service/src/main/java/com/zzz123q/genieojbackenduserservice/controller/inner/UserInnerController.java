package com.zzz123q.genieojbackenduserservice.controller.inner;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zzz123q.genieojbackendmodel.model.entity.User;
import com.zzz123q.genieojbackendserviceclient.service.UserFeignClient;
import com.zzz123q.genieojbackenduserservice.service.UserService;

/**
 * 该接口仅用于项目内部调用
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    /**
     * 根据id获取用户
     * 
     * @param id
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据id获取一组用户
     * 
     * @param ids
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("ids") Collection<Long> ids) {
        return userService.listByIds(ids);
    }

}
