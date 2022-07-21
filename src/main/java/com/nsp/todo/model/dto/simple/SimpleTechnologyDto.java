package com.nsp.todo.model.dto.simple;

import com.nsp.todo.validation.group.AdvancedInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class SimpleTechnologyDto {
    @NotNull(groups = {AdvancedInfo.class})
    @NumberFormat
    private Long id;
}
