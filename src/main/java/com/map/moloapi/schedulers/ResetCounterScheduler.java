package com.map.moloapi.schedulers;

import com.map.moloapi.services.RefreshTokenService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author DIOMANDE Souleymane 
 * @project ebill-sic
 * @Date 02/01/2024 19:18
 */
@EnableScheduling
@Slf4j
@Service
public class ResetCounterScheduler {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Scheduled(cron = "0 0 */2 * * *")
    @PostConstruct
    public void deleteExpriyToken() {
        refreshTokenService.deleteExpiryTokens();
    }
}
