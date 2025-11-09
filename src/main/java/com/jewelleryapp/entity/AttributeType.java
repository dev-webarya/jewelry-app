package com.jewelleryapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter // Replaced @Data
@Setter // Replaced @Data
@ToString(exclude = "values") // Added to break loop
@EqualsAndHashCode(exclude = "values") // Added to break loop
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attribute_types")
public class AttributeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "attributeType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<AttributeValue> values = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}