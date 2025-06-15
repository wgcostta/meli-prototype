package com.mercadoclone.dto.mapper;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.dto.response.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(ProductEntity product);

    List<ProductResponse> toResponseList(List<ProductEntity> products);
}
