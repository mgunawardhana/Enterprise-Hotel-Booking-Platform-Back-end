package com.example.app.application.mapper;

import com.example.app.domain.model.RoomImage;
import com.example.app.infrastructure.persistence.entity.RoomImageEntity;
import org.mapstruct.*;

/**
 * MapStruct mapper for RoomImage domain model and RoomImageEntity.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomImageMapper {
    
    /**
     * Convert RoomImageEntity to RoomImage domain model
     */
    @Mapping(target = "roomId", source = "room.id")
    RoomImage toDomain(RoomImageEntity entity);
    
    /**
     * Convert RoomImage domain model to RoomImageEntity
     * Note: room relationship must be set separately
     */
    @Mapping(target = "room", ignore = true)
    RoomImageEntity toEntity(RoomImage domain);
}
