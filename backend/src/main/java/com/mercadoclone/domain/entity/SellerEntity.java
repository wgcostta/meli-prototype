package com.mercadoclone.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public
/**
 * Entidade que representa um vendedor.
 */
class SellerEntity {
    private String id;
    private String name;
    private Double reputation;
    private String location;

    @JsonProperty("isOfficial")
    private Boolean isOfficial;

    @JsonProperty("positiveRating")
    private Integer positiveRating;

    @JsonProperty("yearsOnPlatform")
    private Integer yearsOnPlatform;

    private String avatar;

    public SellerEntity() {}

    public SellerEntity(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getReputation() { return reputation; }
    public void setReputation(Double reputation) { this.reputation = reputation; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Boolean getIsOfficial() { return isOfficial; }
    public void setIsOfficial(Boolean isOfficial) { this.isOfficial = isOfficial; }

    public Integer getPositiveRating() { return positiveRating; }
    public void setPositiveRating(Integer positiveRating) { this.positiveRating = positiveRating; }

    public Integer getYearsOnPlatform() { return yearsOnPlatform; }
    public void setYearsOnPlatform(Integer yearsOnPlatform) { this.yearsOnPlatform = yearsOnPlatform; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    /**
     * Verifica se o vendedor é confiável (vendedor oficial ou com boa reputação).
     */
    public boolean isTrusted() {
        return Boolean.TRUE.equals(isOfficial) ||
                (reputation != null && reputation >= 4.5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerEntity seller = (SellerEntity) o;
        return Objects.equals(id, seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}