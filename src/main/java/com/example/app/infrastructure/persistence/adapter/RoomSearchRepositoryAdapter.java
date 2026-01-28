package com.example.app.infrastructure.persistence.adapter;

import com.example.app.application.mapper.RoomMapper;
import com.example.app.domain.model.Room;
import com.example.app.domain.port.out.RoomSearchRepositoryPort;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import com.example.app.infrastructure.persistence.entity.RoomEntity;
import com.example.app.infrastructure.persistence.repository.RoomJpaRepository;
import com.example.app.infrastructure.persistence.specification.RoomSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing RoomSearchRepositoryPort.
 * Bridges domain layer with JPA persistence using Specifications.
 */
@Component
public class RoomSearchRepositoryAdapter implements RoomSearchRepositoryPort {
    
    private static final Logger log = LoggerFactory.getLogger(RoomSearchRepositoryAdapter.class);
    
    private final RoomJpaRepository roomJpaRepository;
    private final RoomMapper roomMapper;
    
    public RoomSearchRepositoryAdapter(RoomJpaRepository roomJpaRepository, RoomMapper roomMapper) {
        this.roomJpaRepository = roomJpaRepository;
        this.roomMapper = roomMapper;
    }
    
    @Override
    public Page<Room> findByCriteria(RoomSearchCriteria criteria, Pageable pageable) {
        log.debug("Building specification for room search");
        
        // Build dynamic specification from criteria
        Specification<RoomEntity> specification = RoomSpecification.buildSpecification(criteria);
        
        // Execute query with specification and pagination
        Page<RoomEntity> entityPage = roomJpaRepository.findAll(specification, pageable);
        
        log.debug("Found {} rooms matching criteria", entityPage.getTotalElements());
        
        // Map entities to domain models
        return entityPage.map(roomMapper::toDomain);
    }
}
