package com.example.trecking_time.entity;

import com.example.trecking_time.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
//    @ManyToMany
//    @Setter(value = AccessLevel.PRIVATE)
//    private List<Role> roles;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role = new HashSet<>();
}
