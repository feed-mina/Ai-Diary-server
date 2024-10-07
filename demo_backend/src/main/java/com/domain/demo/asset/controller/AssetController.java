package com.domain.demo.asset.controller;

import com.domain.demo.asset.dto.AssetDto;
import com.domain.demo.asset.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping
    public List<AssetDto> getAllAssets() {
        return assetService.getAllAssets();
    }

    @PostMapping
    public AssetDto createAsset(@RequestBody AssetDto assetDto) {
        return assetService.createAsset(assetDto);
    }

    @PostMapping("/{id}")
    public AssetDto updateAsset(@PathVariable Long id, @RequestBody AssetDto assetDto) {
        return // 호출 시 AssetDto, Long 순서로 전달해야 합니다.
                assetService.updateAsset(id, assetDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}
