package com.mercadoclone.service.command.pattern;

import com.mercadoclone.domain.entity.ProductEntity;
import com.mercadoclone.domain.repository.ProductRepository;
import com.mercadoclone.service.ProductService;

import java.util.List;

public record NoFilter() implements FilterCommand {
    public List<ProductEntity> execute(ProductService service, ProductRepository repository) {
        List<ProductEntity> products = repository.findAll();
        return products;
    }
}