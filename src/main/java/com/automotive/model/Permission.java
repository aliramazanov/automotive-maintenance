package com.automotive.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "http_method")
    private String httpMethod;
    
    @Column(name = "path_pattern")
    private String pathPattern;
    
    @Column(name = "permission_code")
    private String permissionCode;
}
