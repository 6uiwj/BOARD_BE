package org.choongang.commons.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMember {

    @CreatedBy
    @Column(length=80, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length=80, insertable = false)
    private String modifiedBy;
}
