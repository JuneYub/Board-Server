package com.campus.boardserver.boardserver.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.campus.boardserver.boardserver.mapper")
public class MysqlConfig {
}
