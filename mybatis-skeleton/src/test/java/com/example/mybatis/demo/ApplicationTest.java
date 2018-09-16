package com.example.mybatis.demo;

import com.example.mybatis.demo.mybatis.entity.User;
import com.example.mybatis.demo.mybatis.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
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
        assertEquals(i, 1);
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
        System.out.println("list size is : " + list.size());
    }

}
