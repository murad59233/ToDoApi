package com.nsp.todo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date createdDate = new Date();
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
}
