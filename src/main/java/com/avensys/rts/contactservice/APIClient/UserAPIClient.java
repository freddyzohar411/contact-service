package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.interceptor.JwtTokenInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "http://localhost:8090/api/user", configuration = JwtTokenInterceptor.class)
public interface UserAPIClient {

    @GetMapping("/{id}")
    HttpResponse getUserById(@PathVariable("id") Integer id);

    @GetMapping("")
    HttpResponse getUserByEmail(@RequestParam("email") String email);

}

