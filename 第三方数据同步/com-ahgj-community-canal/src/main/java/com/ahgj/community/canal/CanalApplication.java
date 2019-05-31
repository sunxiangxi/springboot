package com.ahgj.community.canal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

//@EnableScheduling
//@EnableConfigurationProperties({CustomConfigurationBean.class})//多个类,隔开
@MapperScan(basePackages = "com.ahgj.community.canal.mapper")
@SpringBootApplication
public class CanalApplication {
    private static final Logger logger = LoggerFactory.getLogger(CanalApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CanalApplication.class, args);
		logger.info("============社区诚信第三方数据同步系统启动成功================");
	}

}
