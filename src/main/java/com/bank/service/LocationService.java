package com.bank.service;


import com.bank.exceptions.ResourceNotFoundException;
import com.bank.models.Location;
import com.bank.models.Post;
import com.bank.props.GeocoderProperties;
import com.bank.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final GeocoderProperties geocoderProperties;
    private final RestTemplate restTemplate;

    @Transactional
    public Location save(String request){
        String coordinates = getCoordinatesByAddress(request);
        Optional<Location> check = locationRepository.findByCoordinates(coordinates);
        if (check.isPresent())
            return check.get();
        Location address = Location.builder()
                .coordinates(coordinates)
                .address(getAddressByCoordinates(coordinates))
                .build();
        return locationRepository.save(address);
    }

    public Location getById(Long id){
        return locationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Address with this not found!"));
    }

    public String getCoordinatesByAddress(String address) {
        String url = generateUrl(address);
        System.out.println("url: "+url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseResponseToCoordinates(response);
    }

    public String getAddressByCoordinates(String coordinates){
        String url = generateUrl(coordinates);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseResponseToAddress(response);
    }

    private String generateUrl(String geocode){
        String url = geocoderProperties.getUrl() + geocode +
                "&" +
                geocoderProperties.getFormat();
        return url;
    }

    private String parseResponseToCoordinates(ResponseEntity<String> response) {
        JSONObject json = new JSONObject(response.getBody());
        JSONObject featureMember = new JSONObject(json
                .getJSONObject("response")
                .getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember")
                .get(0).toString());
        String coordinates = featureMember
                .getJSONObject("GeoObject")
                .getJSONObject("Point").getString("pos");
        return coordinates;
    }


    private String parseResponseToAddress(ResponseEntity<String> response){
        JSONObject json = new JSONObject(response.getBody());
        JSONObject featureMember = new JSONObject(json
                .getJSONObject("response")
                .getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember")
                .get(0).toString());
        String address = featureMember
                .getJSONObject("GeoObject")
                .getJSONObject("metaDataProperty")
                .getJSONObject("GeocoderMetaData")
                .getString("text");
        return address;
    }
}