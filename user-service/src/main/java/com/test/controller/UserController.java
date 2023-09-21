package com.test.controller;

import com.test.entity.User;
import com.test.service.UserService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Resource
    UserService service;

    @RequestMapping("/user/{uid}")
    public User findUserById(@PathVariable("uid") int uid){
        return service.getUserById(uid);
    }

    @RequestMapping("/user/remain/{uid}")
    public int userRemain(@PathVariable("uid") int uid){
        return service.getRemain(uid);
    }

    @RequestMapping("/user/borrow/{uid}")
    public boolean userBorrow(@PathVariable("uid") int uid){
        int remain = service.getRemain(uid);
        return service.setRemain(uid, remain - 1);
    }
}
