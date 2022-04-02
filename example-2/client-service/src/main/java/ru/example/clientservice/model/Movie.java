package ru.example.clientservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Movie {
    private String id;
    private String name;
    private String price;
}