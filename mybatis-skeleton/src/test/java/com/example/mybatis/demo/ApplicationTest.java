package com.example.mybatis.demo;

import com.example.mybatis.demo.mybatis.entity.User;
import com.example.mybatis.demo.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTest {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Test
    public void selectByUserId() {
        User user = userMapper.selectByPrimaryKey(1L);
        assertEquals(user.getName(), "Bob");
    }

    @Test
    public void insertNewUser() {
        User user = new User();
        user.setAge(18);
        user.setName("Mike");
        user.setCreateDate(new Date());
        int i = userMapper.insert(user);
        log.info("the new insert id is {}}", user.getId());
    }

    @Test
    public void updateUserByUserId() {
        User user = new User();
        user.setId(1L);
        user.setAge(30);
        int i = userMapper.updateByPrimaryKeySelective(user);
        assertEquals(i, 1);
    }

    @Test
    public void selectByDate() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = df.parse("2018-09-15 12:12:00");
        Date end = df.parse(df.format(new Date()));
        List<User> list = userMapper.selectByDatePeriod(start, end);
        log.info("list size is : {}", list.size());
    }


    @Test
    public void batchInsert() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("Batch-" + i);
            user.setAge(20 + i);
            user.setCreateDate(new Date());
            list.add(user);
        }

        int i = userMapper.batchInsert(list);
        assertEquals(5, i);
        for (User user : list) {
            assertNotNull(user.getId());
            log.info("user id is : {}", user.getId());
        }
    }

    @Test
    public void selectByMoreParams() {
        Map map = new HashMap<>();
        map.put("age", 18);
        map.put("startDate", "2018-09-15 22:34:02");
        map.put("endDate", "2018-09-16 22:34:02");
        List<User> result = userMapper.selectByMoreParams(map);
        log.info("user list size is : {}", result.size());
    }


}
