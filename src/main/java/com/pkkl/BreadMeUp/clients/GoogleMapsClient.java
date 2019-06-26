package com.pkkl.BreadMeUp.clients;

import com.pkkl.BreadMeUp.dtos.BakeryLocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google", url = "https://maps.googleapis.com/maps/api/place/details")
public interface GoogleMapsClient {

    @GetMapping(
            value = "json"
    )
    BakeryLocationDto getGoogle(@RequestParam("placeid") String placeId, @RequestParam("key") String key);

}
