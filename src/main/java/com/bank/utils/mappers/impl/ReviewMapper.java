package com.bank.utils.mappers.impl;

import com.bank.dto.ReviewDTO;
import com.bank.models.Review;
import com.bank.service.PostService;
import com.bank.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewMapper implements Mappable<Review, ReviewDTO> {
    private final ModelMapper modelMapper;
    private final PostService postService;

    @Override
    public Review fromDTO(ReviewDTO dto) {
        return modelMapper.map(dto, Review.class);
    }

    @Override
    public ReviewDTO toDTO(Review entity) {
        return modelMapper.map(entity, ReviewDTO.class);
    }

    @Override
    public List<Review> fromDTOs(List<ReviewDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<ReviewDTO> toDTOs(List<Review> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
