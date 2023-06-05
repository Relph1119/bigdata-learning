package com.teapot.clickhouse.controller;

import com.teapot.clickhouse.dao.CustomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/custom")
public class CustomController {

    @Autowired
    CustomDao customDao;

    @GetMapping("query")
    public String customQueryId() {
        try {
            return customDao.customQueryId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
