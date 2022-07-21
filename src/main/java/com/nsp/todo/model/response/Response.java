package com.nsp.todo.model.response;

import com.nsp.todo.enums.Status;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String status;
    private Integer code;
    private String message;
    @Builder.Default
    private Set<String> errors = new HashSet<>();

    public Response(Status status) {
        this.status = status.name();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.errors = new HashSet<>();
    }

    public Response(Status status, Set<String> errors){
        this.status = status.name();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.errors = errors;
    }

    public static Response getSuccess(){
        return Response.builder()
                .message(Status.SUCCESS.getMessage())
                .code(Status.SUCCESS.getCode())
                .status(Status.SUCCESS.name())
                .errors(new HashSet<>())
                .build();
    }

    public static Response getNotFound(Status status){
        return Response.builder()
                .message(status.getMessage())
                .code(status.getCode())
                .status(status.name())
                .errors(new HashSet<>())
                .build();
    }
}
