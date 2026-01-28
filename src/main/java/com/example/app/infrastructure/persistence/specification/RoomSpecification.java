package com.example.app.infrastructure.persistence.specification;

import com.example.app.domain.valueobject.BedType;
import com.example.app.domain.valueobject.RoomSearchCriteria;
import com.example.app.domain.valueobject.RoomStatus;
import com.example.app.domain.valueobject.RoomView;
import com.example.app.infrastructure.persistence.entity.BookingEntity;
import com.example.app.infrastructure.persistence.entity.RoomEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification builder for dynamic room search queries.
 * Provides type-safe query construction with performance optimizations.
 */
public class RoomSpecification {
    
    /**
     * Builds a complete specification from search criteria
     */
    public static Specification<RoomEntity> buildSpecification(RoomSearchCriteria criteria) {
        return Specification
                .where(isNotDeleted())
                .and(hasStatus(RoomStatus.AVAILABLE))
                .and(hasPriceBetween(criteria.getMinPrice(), criteria.getMaxPrice()))
                .and(hasMinGuests(criteria.getMinGuests()))
                .and(hasBedTypeIn(criteria.getBedTypes()))
                .and(hasAmenitiesContaining(criteria.getAmenities()))
                .and(hasViewIn(criteria.getViews()))
                .and(isAvailableBetween(criteria.getCheckIn(), criteria.getCheckOut()));
    }
    
    /**
     * Filter out soft-deleted rooms
     */
    public static Specification<RoomEntity> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }
    
    /**
     * Filter by room status
     */
    public static Specification<RoomEntity> hasStatus(RoomStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
    
    /**
     * Filter by price range
     */
    public static Specification<RoomEntity> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice));
            }
            
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice));
            }
            
            return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * Filter by minimum number of guests
     */
    public static Specification<RoomEntity> hasMinGuests(Integer minGuests) {
        return (root, query, cb) -> {
            if (minGuests == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("maxGuests"), minGuests);
        };
    }
    
    /**
     * Filter by bed types (OR condition)
     */
    public static Specification<RoomEntity> hasBedTypeIn(List<BedType> bedTypes) {
        return (root, query, cb) -> {
            if (bedTypes == null || bedTypes.isEmpty()) {
                return null;
            }
            return root.get("bedType").in(bedTypes);
        };
    }
    
    /**
     * Filter by amenities (AND condition - room must have ALL specified amenities)
     */
    public static Specification<RoomEntity> hasAmenitiesContaining(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return null;
            }
            
            // Join with amenities collection
            Join<Object, Object> amenitiesJoin = root.join("amenities", JoinType.LEFT);
            
            // Create predicates for each amenity
            List<Predicate> predicates = new ArrayList<>();
            for (String amenity : amenities) {
                predicates.add(cb.equal(amenitiesJoin, amenity));
            }
            
            // Group by room ID and ensure all amenities are present
            query.groupBy(root.get("id"));
            query.having(cb.greaterThanOrEqualTo(cb.count(root.get("id")), (long) amenities.size()));
            
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
    
    /**
     * Filter by room views (OR condition)
     */
    public static Specification<RoomEntity> hasViewIn(List<RoomView> views) {
        return (root, query, cb) -> {
            if (views == null || views.isEmpty()) {
                return null;
            }
            return root.get("view").in(views);
        };
    }
    
    /**
     * Filter by availability (exclude rooms with overlapping bookings)
     */
    public static Specification<RoomEntity> isAvailableBetween(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, cb) -> {
            if (checkIn == null || checkOut == null) {
                return null;
            }
            
            // Subquery to find rooms with conflicting bookings
            Subquery<String> bookingSubquery = query.subquery(String.class);
            Root<BookingEntity> bookingRoot = bookingSubquery.from(BookingEntity.class);
            
            bookingSubquery.select(bookingRoot.get("roomId").as(String.class))
                    .where(
                            cb.and(
                                    cb.equal(bookingRoot.get("roomId"), root.get("id")),
                                    cb.equal(bookingRoot.get("status"), "CONFIRMED"),
                                    cb.or(
                                            // Booking starts during requested period
                                            cb.and(
                                                    cb.greaterThanOrEqualTo(bookingRoot.get("checkInDate"), checkIn),
                                                    cb.lessThan(bookingRoot.get("checkInDate"), checkOut)
                                            ),
                                            // Booking ends during requested period
                                            cb.and(
                                                    cb.greaterThan(bookingRoot.get("checkOutDate"), checkIn),
                                                    cb.lessThanOrEqualTo(bookingRoot.get("checkOutDate"), checkOut)
                                            ),
                                            // Booking spans entire requested period
                                            cb.and(
                                                    cb.lessThanOrEqualTo(bookingRoot.get("checkInDate"), checkIn),
                                                    cb.greaterThanOrEqualTo(bookingRoot.get("checkOutDate"), checkOut)
                                            )
                                    )
                            )
                    );
            
            // Exclude rooms that have conflicting bookings
            return cb.not(root.get("id").in(bookingSubquery));
        };
    }
}
