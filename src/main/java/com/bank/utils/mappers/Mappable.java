package com.bank.utils.mappers;

import java.util.List;

public interface Mappable<E, D> {
    E fromDTO(D dto);
    D toDTO(E entity);
    List<E> fromDTOs(List<D> dtos);
    List<D> toDTOs(List<E> entities);
}
