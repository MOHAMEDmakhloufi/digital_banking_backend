package com.fsb.digital_banking_backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_username", columnNames = "username")
        }
)
@Data
@NoArgsConstructor @AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            name = "username",
            nullable = false
    )
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(
            name = "password",
            nullable = false
    )
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles= new ArrayList<>();
}
