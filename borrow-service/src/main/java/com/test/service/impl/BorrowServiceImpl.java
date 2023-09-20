package com.test.service.impl;

import com.test.client.BookClient;
import com.test.client.UserClient;
import com.test.entity.Book;
import com.test.entity.Borrow;
import com.test.entity.BorrowDetail;
import com.test.entity.User;
import com.test.mapper.BorrowMapper;
import com.test.service.BorrowService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

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
    public BorrowDetail getBorrowDetailByUid(int uid) {
        List<Borrow> borrows = mapper.getBorrowsByUid(uid);
        User user = userClient.getUserById(uid);
        List<Book> bookList = borrows
                .stream()
                .map(b -> bookClient.getBookById(b.getBid()))
                .collect(Collectors.toList());
        return new BorrowDetail(user, bookList);
    }
}
