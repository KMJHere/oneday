package com.oneday.web.api;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="List")
@RestController
@RequestMapping("/api")
public class TestRestController {

    @GetMapping("/test")
    @Operation(summary = "요약", description = "test api 입니다..")
    public String testApi() {
        return "test..";
    }
}
