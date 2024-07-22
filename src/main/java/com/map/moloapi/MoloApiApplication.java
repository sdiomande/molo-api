package com.map.moloapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class MoloApiApplication implements CommandLineRunner {
    Logger log = Logger.getLogger(MoloApiApplication.class.getName());
    public static void main(String[] args) {
        SpringApplication.run(MoloApiApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info(">>>>> MOLO API STARTED SUCCESFULLY <<<<<");
    }

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<?> getInformation() throws UnknownHostException {
        log.info("-- Debut recuperation des informations --");
        return new ResponseEntity<>(Map.of(
                "hasError", false,
                "message", "Success",
                "data", Map.of(
                        "ip", Inet4Address.getLocalHost().getHostAddress(),
                        "name", "DIOMANDE Souleymane"
                )), HttpStatus.OK);
    }



}
