package com.test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import com.test.entity.User;

@Data
@AllArgsConstructor
public class BorrowDetail {

    User user;
    List<Book> bookList;
}
