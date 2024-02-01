package com.bank.utils.mappers.impl;

import com.bank.dto.LocationDTO;
import com.bank.models.Location;
import com.bank.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddressMapper implements Mappable<Location, LocationDTO> {
    private final ModelMapper modelMapper;
    @Override
    public Location fromDTO(LocationDTO dto) {
        return modelMapper.map(dto, Location.class);
    }

    @Override
    public LocationDTO toDTO(Location entity) {
        return modelMapper.map(entity, LocationDTO.class);
    }

    @Override
    public List<Location> fromDTOs(List<LocationDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<LocationDTO> toDTOs(List<Location> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
