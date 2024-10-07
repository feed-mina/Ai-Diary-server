package com.domain.demo.asset.repository;

import com.domain.demo.asset.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {

}
