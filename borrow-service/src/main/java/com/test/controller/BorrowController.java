package com.test.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSONObject;
import com.test.entity.BorrowDetail;
import com.test.entity.User;
import com.test.service.BorrowService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class BorrowController {

    @Resource
    BorrowService service;

    @RequestMapping("/borrow/{uid}")
    BorrowDetail findUserBorrows(@PathVariable("uid") int uid){
        return service.getBorrowDetailByUid(uid);
    }


    //慢调用
/*    @RequestMapping("/borrow2/{uid}")
    BorrowDetail findUserBorrows2(@PathVariable("uid") int uid) throws InterruptedException {
        Thread.sleep(1000);
        return null;
    }*/

    //异常比例
/*    @RequestMapping("/borrow2/{uid}")
    BorrowDetail findUserBorrows2(@PathVariable("uid") int uid) {
        throw new RuntimeException();
    }*/

    //自定义熔断处理
    @RequestMapping("/borrow2/{uid}")
    @SentinelResource(value = "findUserBorrows2", blockHandler = "test")
    BorrowDetail findUserBorrows2(@PathVariable("uid") int uid) {
        throw new RuntimeException();
    }

    BorrowDetail test(int uid, BlockException e){
        return new BorrowDetail(new User(), Collections.emptyList());
    }

    @RequestMapping("/blocked")
    JSONObject blocked(){
        JSONObject object = new JSONObject();
        object.put("code", 403);
        object.put("success", false);
        object.put("massage", "您的请求频率过快，请稍后再试！");
        return object;
    }


    @RequestMapping("/test")
    @SentinelResource("test")   //注意这里需要添加@SentinelResource才可以，用户资源名称就使用这里定义的资源名称
    String findUserBorrows2(@RequestParam(value = "a", required = false) Integer a,
                            @RequestParam(value = "b", required = false) Integer b,
                            @RequestParam(value = "c",required = false) Integer c) {
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        return "请求成功！";
    }


    //事务
    @RequestMapping("/borrow/take/{uid}/{bid}")
    JSONObject borrow(@PathVariable("uid") int uid,
                      @PathVariable("bid") int bid){
        service.doBorrow(uid, bid);

        JSONObject object = new JSONObject();
        object.put("code", "200");
        object.put("success", false);
        object.put("message", "借阅成功！");
        return object;
    }
}
