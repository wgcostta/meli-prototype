package com.mercadoclone.service.command.pattern;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.dto.request.FilterRequest;
import com.mercadoclone.service.ProductService;
import io.micrometer.common.util.StringUtils;

import java.util.List;

public sealed interface FilterCommand
        permits CategoryFilter, BrandFilter, SearchFilter, AvailableFilter,
        DiscountedFilter, PriceRangeFilter, NoFilter {

    List<ProductEntity> execute(ProductService service, ProductRepository repository);

    static FilterCommand fromRequest(FilterRequest filter) {
        if (StringUtils.isNotBlank(filter.categoryId()))
            return new CategoryFilter(filter.categoryId());
        if (StringUtils.isNotBlank(filter.brandId()))
            return new BrandFilter(filter.brandId());
        if (StringUtils.isNotBlank(filter.value()))
            return new SearchFilter(filter.value());
        if (filter.available() != null && filter.available())
            return new AvailableFilter();
        if (filter.discounted() != null && filter.discounted())
            return new DiscountedFilter();
        if (filter.rangePrice() != null && filter.rangePrice())
            return new PriceRangeFilter(filter.minPrice(), filter.maxPrice());
        return new NoFilter();
    }
}


