package com.nsp.todo.model.dto.simple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SimpleTaskDto {
    @NotNull
    @NumberFormat
    private Long id;
}
