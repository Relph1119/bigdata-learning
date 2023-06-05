package com.teapot.clickhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DbClickhouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbClickhouseApplication.class, args);
    }

}
