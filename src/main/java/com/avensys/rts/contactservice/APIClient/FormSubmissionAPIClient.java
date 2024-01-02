package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.interceptor.JwtTokenInterceptor;
import com.avensys.rts.contactservice.payloadnewrequest.FormSubmissionsRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "form-service", url = "${api.form-submission.url}", configuration = JwtTokenInterceptor.class)
public interface FormSubmissionAPIClient {

	@PostMapping("/form-submissions")
	HttpResponse addFormSubmission(@RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);

	@GetMapping("/form-submissions/{formSubmissionId}")
	HttpResponse getFormSubmission(@PathVariable Long formSubmissionId);

	@PutMapping("/form-submissions/{formSubmissionId}")
	HttpResponse updateFormSubmission(@PathVariable Long formSubmissionId,
			@RequestBody FormSubmissionsRequestDTO formSubmissionsRequestDTO);

	@DeleteMapping("/form-submissions/{formSubmissionId}")
	HttpResponse deleteFormSubmission(@PathVariable Long formSubmissionId);

}
