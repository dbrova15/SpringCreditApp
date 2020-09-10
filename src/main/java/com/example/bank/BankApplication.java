package com.example.bank;

import com.example.bank.controller.ClientControler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.Directory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);

		Logger logger = LogManager.getLogger(ClientControler.class);

		File dir = new File("logs");

		if (!dir.exists()) {
			dir.mkdir();
		}

		File f = new File("logs/logfile.log");
		try {
			if(!f.exists()) {
				f.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("ERROR: " + e.getMessage());
		}
	}

}
