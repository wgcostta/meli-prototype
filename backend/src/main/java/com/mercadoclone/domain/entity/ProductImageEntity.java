package com.mercadoclone.domain.entity;

import java.util.Objects;

/**
 * Entidade que representa uma imagem do produto.
 */
public class ProductImageEntity {
    private String id;
    private String url;
    private String alt;
    private Integer order;

    public ProductImageEntity() {}

    public ProductImageEntity(String id, String url, String alt, Integer order) {
        this.id = id;
        this.url = url;
        this.alt = alt;
        this.order = order;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getAlt() { return alt; }
    public void setAlt(String alt) { this.alt = alt; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImageEntity that = (ProductImageEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


