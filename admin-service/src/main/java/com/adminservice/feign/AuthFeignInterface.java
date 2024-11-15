package com.adminservice.feign;

import com.adminservice.global.GetTokenInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", path = "/api/auth")
public interface AuthFeignInterface {

    // Access Token 으로 Member 정보 가져오기
    @GetMapping("/token")
    GetTokenInfo getInfo(@RequestParam String token);

}
