package com.test.service;

import com.test.entity.BorrowDetail;

public interface BorrowService {

    public BorrowDetail getBorrowDetailByUid(int uid);

    boolean doBorrow(int uid, int bid);
}
