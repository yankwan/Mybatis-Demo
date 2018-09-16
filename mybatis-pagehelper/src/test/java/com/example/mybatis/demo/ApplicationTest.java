package com.example.mybatis.demo;

import com.example.mybatis.demo.mybatis.entity.User;
import com.example.mybatis.demo.mybatis.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectUsers1() {
        PageHelper.startPage(1, 3);
        List<User> list = userMapper.selectUsers();
        assertEquals(3, list.size());

        // 获取分页信息
        PageInfo<User> pageInfo = new PageInfo<User>(list);
        log.info("The total is : {}", pageInfo.getTotal());
        log.info("The page size is : {}", pageInfo.getPageSize());
        log.info("The page num is :{}", pageInfo.getPageNum());
    }

    @Test
    public void selectUsers2() {
        PageHelper.startPage(1, 3);
        List<User> list = userMapper.selectUsers();
        assertEquals(3, list.size());
    }



}
