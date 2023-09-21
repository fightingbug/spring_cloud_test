package com.test.mapper;

import com.test.entity.Borrow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowMapper {

    @Select("select * from tb_borrow where uid = #{uid}")
    List<Borrow> getBorrowsByUid(int uid);

    @Select("select * from tb_borrow where bid = #{bid}")
    List<Borrow> getBorrowsByBid(int bid);

    @Select("select * from tb_borrow where uid = #{uid} and bid = #{bid}")
    Borrow getBorrow(int uid,int bid);

    @Insert("insert into db_borrow(uid,bid) values(#{uid},#{bid})")
    int addBorrow(int uid, int bid);
}
