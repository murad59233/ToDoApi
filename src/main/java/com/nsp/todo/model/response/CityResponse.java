package com.nsp.todo.model.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityResponse {
    @Builder.Default
    private Long id = 0L;
    @Builder.Default
    private String cityName = "undefined";
}
