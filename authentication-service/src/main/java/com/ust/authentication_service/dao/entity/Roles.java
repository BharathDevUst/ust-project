package com.ust.authentication_service.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "roles_details")
public class Roles {
    @Id
    @Column(name = "role_id")
    private int role_id;

    @Column(name = "role_name")
    private String roleName;
}
