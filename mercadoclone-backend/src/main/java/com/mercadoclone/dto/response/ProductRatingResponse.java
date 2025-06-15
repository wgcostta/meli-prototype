package com.mercadoclone.dto.response;


import java.util.Map;

public record ProductRatingResponse (
        Double average,
        Integer count,
        Map<String, Integer> distribution
) {

}
