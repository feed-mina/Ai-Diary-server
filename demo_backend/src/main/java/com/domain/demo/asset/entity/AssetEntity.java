package com.domain.demo.asset.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="assets")
@Getter
@Setter
@NoArgsConstructor
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 자산 종류 ( 현금, 주식, 예금등
    private Double value;

    public AssetEntity(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
