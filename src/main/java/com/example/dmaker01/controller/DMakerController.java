package com.example.dmaker01.controller;

import com.example.dmaker01.dto.*;
import com.example.dmaker01.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody final CreateDeveloper.Request request
    ) {
        log.info("request : {}", request);

        return dMakerService.createDeveloper(request);
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable final String memberId
    ) {
        log.info("GET /developer/" + memberId + " HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }


    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable final String memberId,
            @Valid @RequestBody final EditDeveloper.Request request
    ) {
        log.info("PUT /developer/" + memberId + " HTTP/1.1");

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public RetiredDeveloperDto deleteDeveloper(
            @PathVariable final String memberId
    ) {
        log.info("DELETE /developer/" + memberId + " HTTP/1.1");

        return dMakerService.deleteDeveloper(memberId);
    }
}
