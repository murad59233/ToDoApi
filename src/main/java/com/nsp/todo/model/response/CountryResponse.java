package com.nsp.todo.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {
    @Builder.Default
    private Long id = 1L;
    @Builder.Default
    private String countryName = "undefined";
}
