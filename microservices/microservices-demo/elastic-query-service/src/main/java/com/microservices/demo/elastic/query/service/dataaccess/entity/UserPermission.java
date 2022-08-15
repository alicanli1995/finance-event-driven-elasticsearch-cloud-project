package com.microservices.demo.elastic.query.service.dataaccess.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@DynamicUpdate
@RequiredArgsConstructor
public class UserPermission {

    @NotNull
    @Id
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String documentId;

    @NotNull
    private String permissionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPermission that = (UserPermission) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
