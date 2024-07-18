package com.map.moloapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MoloApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MoloApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>> MOLO API STARTED SUCCESFULLY <<<<<");
    }
}
