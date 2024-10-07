package com.domain.demo.asset.mapper;

import com.domain.demo.asset.dto.AssetDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AssetMapper {
    @Select("SELECT id, name, value FROM assets")
    List<AssetDto> getAllAssets();
}

