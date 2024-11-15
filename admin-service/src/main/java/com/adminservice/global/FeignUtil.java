package com.adminservice.global;

import com.adminservice.feign.AuthFeignInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignUtil {
    private final AuthFeignInterface authFeignInterface;

    public GetTokenInfo getTokenInfo(String auth) {
        return authFeignInterface.getInfo(auth.substring(7));
    }

}
