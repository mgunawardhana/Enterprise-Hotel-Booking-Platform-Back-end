package com.example.app.application.mapper;

import com.example.app.domain.model.Room;
import com.example.app.infrastructure.persistence.entity.RoomEntity;
import org.mapstruct.*;

/**
 * MapStruct mapper for Room domain model and RoomEntity.
 * Handles bidirectional mapping between domain and persistence layers.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    
    /**
     * Convert RoomEntity to Room domain model
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "pricePerNight", source = "pricePerNight")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "maxGuests", source = "maxGuests")
    @Mapping(target = "bedType", source = "bedType")
    @Mapping(target = "roomSize", source = "roomSize")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "amenities", source = "amenities")
    @Mapping(target = "badges", source = "badges")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedBy", source = "updatedBy")
    @Mapping(target = "deleted", source = "deleted")
    Room toDomain(RoomEntity entity);
    
    /**
     * Convert Room domain model to RoomEntity
     */
    @Mapping(target = "images", ignore = true)
    RoomEntity toEntity(Room domain);
    
    /**
     * Update existing RoomEntity from Room domain model
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateEntity(Room domain, @MappingTarget RoomEntity entity);
}
