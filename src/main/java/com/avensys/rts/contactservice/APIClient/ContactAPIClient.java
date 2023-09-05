package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "geo-service", url = "http://localhost:8600/geo")
public interface ContactAPIClient {

    @GetMapping("/country-currency/{countryId}")
    HttpResponse getCountryCurrencyById(@PathVariable Integer countryId);

}
