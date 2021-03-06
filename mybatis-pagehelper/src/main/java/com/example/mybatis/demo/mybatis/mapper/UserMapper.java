package com.example.mybatis.demo.mybatis.mapper;

import com.example.mybatis.demo.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * custom mapper
     */
    List<User> selectByDatePeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int batchInsert(List<User> users);

    List<User> selectUsers();

    List<User> selectUsersRowBounds(RowBounds rowBounds);
}