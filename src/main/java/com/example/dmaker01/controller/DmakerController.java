package com.example.dmaker01.controller;

import com.example.dmaker01.dto.CreateDeveloper;
import com.example.dmaker01.dto.DeveloperDetailDto;
import com.example.dmaker01.dto.DeveloperDto;
import com.example.dmaker01.dto.EditDeveloper;
import com.example.dmaker01.service.DmakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DmakerController {
    private final DmakerService dmakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dmakerService.getAllDevelopers();
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        log.info("request : {}", request);

        return dmakerService.createDeveloper(request);
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(
            @PathVariable String memberId
    ){
        log.info("GET /developer/"+memberId+" HTTP/1.1");

        return dmakerService.getDeveloperDetail(memberId);
    }


    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ){
        log.info("PUT /developer/"+memberId+" HTTP/1.1");

        return dmakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDto deleteDeveloper(
            @PathVariable String memberId
    ){
        log.info("DELETE /developer/"+memberId+" HTTP/1.1");

        return dmakerService.deleteDeveloper(memberId);
    }
}
