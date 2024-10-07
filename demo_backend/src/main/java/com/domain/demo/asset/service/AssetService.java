package com.domain.demo.asset.service;

import com.domain.demo.asset.dto.AssetDto;
import com.domain.demo.asset.entity.AssetEntity;
import com.domain.demo.asset.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<AssetDto> getAllAssets() {
        return assetRepository.findAll()
                .stream()
                .map(asset -> new AssetDto(asset.getId(), asset.getName(), asset.getValue()))
                .collect(Collectors.toList());
    }

    public AssetDto createAsset(AssetDto assetDto) {
        AssetEntity asset = new AssetEntity(assetDto.getName(), assetDto.getValue());
        AssetEntity savedAsset = assetRepository.save(asset);
        return new AssetDto(savedAsset.getId(), savedAsset.getName(), savedAsset.getValue());
    }

    public AssetDto updateAsset(Long id, AssetDto assetDto) {
        // 자산 찾기 및 업데이트 로직
        AssetEntity asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));

        // 자산 정보 업데이트
        asset.setName(assetDto.getName());
        asset.setValue(assetDto.getValue());

        // 업데이트된 자산 저장
        AssetEntity updatedAsset = assetRepository.save(asset);

        // AssetDto로 반환
        AssetDto updatedAssetDto = new AssetDto();
        updatedAssetDto.setId(updatedAsset.getId());
        updatedAssetDto.setName(updatedAsset.getName());
        updatedAssetDto.setValue(updatedAsset.getValue());

        return updatedAssetDto;
    }

    public void deleteAsset(Long id) {
        AssetEntity asset = assetRepository.findById(id).orElseThrow(() -> new RuntimeException("Asset not found"));
        assetRepository.delete(asset);
    }

}
