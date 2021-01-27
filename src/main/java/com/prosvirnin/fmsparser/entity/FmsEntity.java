package com.prosvirnin.fmsparser.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "fms")
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class FmsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    @NonNull
    private Long version;

    @Column(name = "name", unique = true)
    @NonNull
    private String name;

}
