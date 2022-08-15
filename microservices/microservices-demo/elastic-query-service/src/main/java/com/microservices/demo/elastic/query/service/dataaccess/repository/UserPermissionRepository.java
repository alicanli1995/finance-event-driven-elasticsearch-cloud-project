package com.microservices.demo.elastic.query.service.dataaccess.repository;

import com.microservices.demo.elastic.query.service.dataaccess.entity.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

    @Query(nativeQuery = true, value =
            "select p.user_permission_id as id, u.username, d.document_id, p.permission_type " +
                    "from users u, user_permissions p, documents d " +
                    "where u.id = p.user_id " +
                    "and d.id = p.document_id " +
                    "and u.username = :username")
    Optional<List<UserPermission>> findUserPermissionByUsername(@Param("username") String username);
}
