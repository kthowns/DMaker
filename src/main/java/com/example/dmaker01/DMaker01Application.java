package com.example.dmaker01;

import com.example.dmaker01.exception.DMakerErrorCode;
import com.example.dmaker01.exception.DMakerException;
import com.example.dmaker01.type.DeveloperLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class DMaker01Application {
    public static void main(String[] args) {
        SpringApplication.run(DMaker01Application.class, args);
    }
}
