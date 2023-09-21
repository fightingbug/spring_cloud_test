package com.test.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.test.client.BookClient;
import com.test.client.UserClient;
import com.test.entity.Book;
import com.test.entity.Borrow;
import com.test.entity.BorrowDetail;
import com.test.entity.User;
import com.test.mapper.BorrowMapper;
import com.test.service.BorrowService;
import javax.annotation.Resource;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    BorrowMapper mapper;

    @Resource
    UserClient userClient;

    @Resource
    BookClient bookClient;

    @Override
    @SentinelResource(value = "getBorrow",blockHandler = "blocked")
    public BorrowDetail getBorrowDetailByUid(int uid) {
        List<Borrow> borrows = mapper.getBorrowsByUid(uid);
        User user = userClient.getUserById(uid);
        List<Book> bookList = borrows
                .stream()
                .map(b -> bookClient.getBookById(b.getBid()))
                .collect(Collectors.toList());
        return new BorrowDetail(user, bookList);
    }


    @GlobalTransactional
    @Override
    public boolean doBorrow(int uid, int bid) {
        //1. 判断图书和用户是否都支持借阅
        if(bookClient.bookRemain(bid) < 1)
            throw new RuntimeException("图书数量不足");
        if(userClient.userRemain(uid) < 1)
            throw new RuntimeException("用户借阅量不足");
        //2. 首先将图书的数量-1
        if(!bookClient.bookBorrow(bid))
            throw new RuntimeException("在借阅图书时出现错误！");
        //3. 添加借阅信息
        if(mapper.getBorrow(uid, bid) != null)
            throw new RuntimeException("此书籍已经被此用户借阅了！");
        if(mapper.addBorrow(uid, bid) <= 0)
            throw new RuntimeException("在录入借阅信息时出现错误！");
        //4. 用户可借阅-1
        if(!userClient.userBorrow(uid))
            throw new RuntimeException("在借阅时出现错误！");
        //完成
        return true;
    }


    //替代方案，注意参数和返回值需要保持一致，并且参数最后还需要额外添加一个BlockException
    public BorrowDetail blocked(int uid, BlockException e) {
        return new BorrowDetail(null, Collections.emptyList());
    }
}
