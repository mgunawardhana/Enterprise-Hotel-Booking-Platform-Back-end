package com.example.app.application.mapper;

import com.example.app.adapter.web.request.RoomSearchRequest;
import com.example.app.adapter.web.response.RoomSearchResponse;
import com.example.app.domain.model.Room;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for room search DTOs.
 * Handles mapping between request/response DTOs and domain objects.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomSearchMapper {
    
    /**
     * Convert RoomSearchRequest to RoomSearchCriteria
     */
    RoomSearchCriteria toSearchCriteria(RoomSearchRequest request);
    
    /**
     * Convert Room domain model to RoomSearchResponse
     * Note: mainImage will be set separately in the controller
     */
    @Mapping(target = "mainImage", ignore = true)
    RoomSearchResponse toSearchResponse(Room room);
}
