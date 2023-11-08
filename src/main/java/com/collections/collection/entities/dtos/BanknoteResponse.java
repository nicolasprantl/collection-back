package com.collections.collection.entities.dtos;

import lombok.Data;

@Data
public class BanknoteResponse {
    private Long id;
    private String issueDate;
    private String country;
    private Double denomination;
    private String series;
    private String description;
    private String frontImageUrl;
    private String backImageUrl;
}
