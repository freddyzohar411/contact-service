package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.interceptor.JwtTokenInterceptor;
import com.avensys.rts.contactservice.payloadnewrequest.FormSubmissionsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "form-service", url = "http://localhost:9400", configuration = JwtTokenInterceptor.class)
public interface FormSubmissionAPIClient {
    @PostMapping("/form-submissions")
    HttpResponse addFormSubmission(@RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);

    @GetMapping("/form-submissions/{formSubmissionId}")
    HttpResponse getFormSubmission(@PathVariable int formSubmissionId);

    @PutMapping("/form-submissions/{formSubmissionId}")
    HttpResponse updateFormSubmission(@PathVariable int formSubmissionId, @RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);
}
