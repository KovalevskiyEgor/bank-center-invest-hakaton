package com.bank.utils.mappers.impl;

import com.bank.dto.PostDTO;
import com.bank.models.Post;
import com.bank.utils.mappers.Mappable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper implements Mappable<Post, PostDTO> {
    private final ModelMapper modelMapper;

    @Override
    public Post fromDTO(PostDTO dto) {
        return modelMapper.map(dto, Post.class);
    }

    @Override
    public PostDTO toDTO(Post entity) {
        return modelMapper.map(entity, PostDTO.class);
    }

    @Override
    public List<Post> fromDTOs(List<PostDTO> dtos) {
        return dtos.stream().map(this::fromDTO).toList();
    }

    @Override
    public List<PostDTO> toDTOs(List<Post> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
}
