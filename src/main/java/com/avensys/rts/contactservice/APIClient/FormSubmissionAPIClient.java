package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.interceptor.JwtTokenInterceptor;
import com.avensys.rts.contactservice.payloadnewrequest.FormSubmissionsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "form-service", url = "${api.form-submission.url}", configuration = JwtTokenInterceptor.class)
public interface FormSubmissionAPIClient {

	@PostMapping("")
	HttpResponse addFormSubmission(@RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);

	@GetMapping("/{formSubmissionId}")
	HttpResponse getFormSubmission(@PathVariable Long formSubmissionId);

	@PutMapping("/{formSubmissionId}")
	HttpResponse updateFormSubmission(@PathVariable Long formSubmissionId,
			@RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);

	@DeleteMapping("/{formSubmissionId}")
	HttpResponse deleteFormSubmission(@PathVariable Long formSubmissionId);

}
