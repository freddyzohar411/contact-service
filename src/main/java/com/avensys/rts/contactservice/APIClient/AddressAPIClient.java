package com.avensys.rts.contactservice.APIClient;

import com.avensys.rts.contactservice.customresponse.HttpResponse;
import com.avensys.rts.contactservice.payloadrequest.AddressRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * author Koh He Xiang
 * This class is an interface to interact with address microservice
 */
@FeignClient(name = "address-service", url = "http://localhost:8300")
public interface AddressAPIClient {
    @PostMapping("/addresses")
    HttpResponse createAddress(@Valid @RequestBody AddressRequestDTO addressRequest);

    @GetMapping("/addresses/{addressId}")
    HttpResponse getAddressById(@PathVariable int addressId);

    @PutMapping("/addresses/{addressId}")
    HttpResponse updateAddress(@PathVariable int addressId, @RequestBody AddressRequestDTO address);

    @DeleteMapping("/addresses/{addressId}")
    HttpResponse deleteAddressById(@PathVariable int addressId);

}
