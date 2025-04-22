package com.courses.java.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthCheckController {

    @GetMapping("alive")
    @ResponseStatus(HttpStatus.OK)
    public void isAlive() {
        log.info("Liveness check touched...");
    }

    @GetMapping("ready")
    @ResponseStatus(HttpStatus.OK)
    public void isReady() {
        log.info("Readiness check touched...");
    }

    @GetMapping("started")
    @ResponseStatus(HttpStatus.OK)
    public void isStarted() {
        log.info("Startup check touched...");
    }
}
