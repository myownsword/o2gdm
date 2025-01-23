package com.tp.o2gdm.o2gdmcore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.tp.o2gdm"})
@MapperScan({"com.tp.o2gdm.o2gdmcore.mapper"})
@EnableScheduling
public class O2gdmCoreApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(com.tp.o2gdm.o2gdmcore.O2gdmCoreApplication.class, args);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
