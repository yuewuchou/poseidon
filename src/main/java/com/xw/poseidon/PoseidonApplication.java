package com.xw.poseidon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这个是sirius项目的升级,期望搭建以spring-boot为后台，以mysql为数据库,以vue为前台，加入微信公众号和小程序的整体解决方案
 * 覆盖的功能点:
 * 1.自动生成三层代码; 已完成
 * 2.连接到数据库;已完成
 * 3.统一controller返回格式;已完成
 * 
 * 11.mysql时区处理; 已完成
 * 16.事务;已完成
 * 7.日志;已完成
 * 13.httpClient;已完成
 * 9.quartz;
 * 10.batch;
 * activiti
 * 
 * 数据库缓存; 暂不处理
 * mybatis缓存;  暂不处理
 * redis;
 * 
 * 线程,线程池
 * 
 * 4.加入工具类:StringUtils,DateUtils....
 * 5.统一异常处理;
 * 6.安全模块:用户权限，数据加密，数字签名....;
 * 
 * 8.多服务器部署;
 * 12.单元测试;
 * 14.mq;
 * 
 * 基于spring boot的多模块依赖
 */
@SpringBootApplication
@MapperScan("com.xw.poseidon.dao")
public class PoseidonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoseidonApplication.class, args);
	}

}
