# Room Search API Documentation

## Overview

The Room Search API provides a powerful, public endpoint for searching and filtering hotel rooms with advanced capabilities including price range filtering, guest capacity, bed types, amenities, room views, and availability checking.

## Endpoint

```
GET /api/v1/rooms/search
```

**Authentication:** None required (Public endpoint)

## Query Parameters

All parameters are optional. If no parameters are provided, all available rooms will be returned.

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `minPrice` | BigDecimal | Minimum price per night | `100.00` |
| `maxPrice` | BigDecimal | Maximum price per night | `500.00` |
| `minGuests` | Integer | Minimum number of guests the room can accommodate | `2` |
| `bedTypes` | List<BedType> | Comma-separated bed types | `KING,QUEEN` |
| `amenities` | List<String> | Comma-separated amenities (room must have ALL) | `Free WiFi,Ocean View` |
| `views` | List<RoomView> | Comma-separated room views | `OCEAN_VIEW,GARDEN_VIEW` |
| `checkIn` | LocalDate | Check-in date (ISO format) | `2026-02-01` |
| `checkOut` | LocalDate | Check-out date (ISO format) | `2026-02-05` |
| `sortBy` | String | Sort field: `price`, `rating`, `popularity` | `price` |
| `sortDirection` | String | Sort direction: `ASC` or `DESC` | `ASC` |
| `page` | Integer | Page number (0-indexed) | `0` |
| `size` | Integer | Page size (1-100) | `10` |

### Bed Types

- `SINGLE` - Single Bed
- `DOUBLE` - Double Bed
- `QUEEN` - Queen Bed
- `KING` - King Bed
- `TWO_QUEEN` - Two Queen Beds
- `TWIN` - Twin Beds

### Room Views

- `OCEAN_VIEW` - Ocean View
- `GARDEN_VIEW` - Garden View
- `PANORAMIC_VIEW` - Panoramic View

### Supported Amenities

- Air Conditioning
- Free WiFi
- Mini Bar
- Ocean View
- Pool Access
- Private Balcony
- Butler Service
- Jacuzzi
- Smart TV
- Kitchen
- Work Desk

## Response Format

```json
{
  "success": true,
  "message": "Rooms fetched successfully",
  "statusCode": 200,
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "title": "Deluxe Ocean View Suite",
        "description": "Spacious suite with panoramic ocean views",
        "pricePerNight": 299.99,
        "rating": 4.8,
        "maxGuests": 4,
        "bedType": "KING",
        "roomSize": 45.5,
        "amenities": ["Free WiFi", "Ocean View", "Private Balcony"],
        "view": "OCEAN_VIEW",
        "mainImage": "https://example.com/images/room1.jpg",
        "badges": ["Popular", "Best Value"]
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 120,
    "totalPages": 12,
    "first": true,
    "last": false
  },
  "timestamp": "2026-01-28T20:50:00",
  "traceId": "abc123def456"
}
```

## Example Requests

### 1. Basic Search (All Rooms)

```bash
GET /api/v1/rooms/search?page=0&size=10
```

### 2. Filter by Price Range

```bash
GET /api/v1/rooms/search?minPrice=100&maxPrice=500&page=0&size=10
```

### 3. Filter by Guests and Bed Type

```bash
GET /api/v1/rooms/search?minGuests=2&bedTypes=KING,QUEEN&page=0&size=10
```

### 4. Filter by Multiple Amenities

```bash
GET /api/v1/rooms/search?amenities=Free WiFi,Ocean View,Private Balcony&page=0&size=10
```

### 5. Filter by Availability

```bash
GET /api/v1/rooms/search?checkIn=2026-02-01&checkOut=2026-02-05&page=0&size=10
```

### 6. Combined Filters with Sorting

```bash
GET /api/v1/rooms/search?minPrice=200&maxPrice=600&minGuests=2&bedTypes=KING&amenities=Free WiFi,Ocean View&views=OCEAN_VIEW&checkIn=2026-02-01&checkOut=2026-02-05&sortBy=price&sortDirection=ASC&page=0&size=20
```

### 7. Sort by Price (Descending)

```bash
GET /api/v1/rooms/search?sortBy=price&sortDirection=DESC&page=0&size=10
```

### 8. Sort by Rating

```bash
GET /api/v1/rooms/search?sortBy=rating&sortDirection=DESC&page=0&size=10
```

## Error Responses

### 400 Bad Request - Invalid Parameters

```json
{
  "success": false,
  "message": "minPrice must be less than or equal to maxPrice",
  "statusCode": 400,
  "data": null,
  "timestamp": "2026-01-28T20:50:00",
  "traceId": "abc123def456"
}
```

### 422 Unprocessable Entity - Validation Errors

```json
{
  "success": false,
  "message": "Validation failed",
  "statusCode": 422,
  "data": {
    "minPrice": "minPrice must be non-negative",
    "size": "size must not exceed 100"
  },
  "timestamp": "2026-01-28T20:50:00",
  "traceId": "abc123def456"
}
```

### 500 Internal Server Error

```json
{
  "success": false,
  "message": "An unexpected error occurred. Please contact support with trace ID: abc123def456",
  "statusCode": 500,
  "data": null,
  "timestamp": "2026-01-28T20:50:00",
  "traceId": "abc123def456"
}
```

## Validation Rules

1. **Price Range**: `minPrice` ≤ `maxPrice`
2. **Date Range**: `checkIn` < `checkOut`
3. **Pagination**: 
   - `page` ≥ 0
   - `1` ≤ `size` ≤ `100`
4. **Sort Direction**: Must be `ASC` or `DESC`

## Performance Optimizations

The API is optimized for high performance:

1. **Database Indexes**: Indexed columns include price, rating, maxGuests, status, view
2. **JPA Specifications**: Dynamic query building without N+1 queries
3. **DTO Projections**: Fetches only required fields
4. **Lazy Loading**: Main image only for listing view
5. **HikariCP**: Optimized connection pooling
6. **Availability Subquery**: Efficient booking overlap detection

## Architecture

The implementation follows **Hexagonal Architecture (Ports & Adapters)**:

```
SearchRoomController (Adapter)
    ↓
SearchRoomsUseCase (Input Port)
    ↓
RoomSearchService (Application)
    ↓
RoomSearchRepositoryPort (Output Port)
    ↓
RoomSearchRepositoryAdapter (Adapter)
    ↓
RoomJpaRepository + RoomSpecification (Infrastructure)
```

## Testing with cURL

### Basic Search
```bash
curl -X GET "http://localhost:8080/api/v1/rooms/search?page=0&size=10"
```

### Filter by Price and Guests
```bash
curl -X GET "http://localhost:8080/api/v1/rooms/search?minPrice=100&maxPrice=500&minGuests=2"
```

### Filter by Availability
```bash
curl -X GET "http://localhost:8080/api/v1/rooms/search?checkIn=2026-02-01&checkOut=2026-02-05"
```

### Complex Query
```bash
curl -X GET "http://localhost:8080/api/v1/rooms/search?minPrice=200&bedTypes=KING,QUEEN&amenities=Free%20WiFi,Ocean%20View&sortBy=price&sortDirection=ASC&page=0&size=20"
```

## Testing with Postman

1. Create a new GET request
2. URL: `http://localhost:8080/api/v1/rooms/search`
3. Add query parameters in the Params tab
4. Send request
5. Verify response structure and data

## Database Setup

The API requires the following database tables:

1. **rooms** - Main room data with indexes
2. **room_amenities** - Room amenities (ElementCollection)
3. **bookings** - Booking data for availability checking

Run the migrations in `src/main/resources/db/migration/`:
- `V3__create_bookings_table.sql`
- `V4__add_room_view_and_indexes.sql`

Or let Hibernate auto-create tables (configured with `ddl-auto: update`).

## Logging

The API logs:
- Incoming search requests with all parameters
- Query execution time
- Total results found
- Pagination details
- Errors with trace IDs

Example log:
```
2026-01-28 20:50:00 - [abc123] Room search request received: minPrice=100, maxPrice=500...
2026-01-28 20:50:00 - [abc123] Room search completed in 45ms. Found 120 rooms (page 0 of 12)
```

## Notes

- All rooms with `deleted=true` are automatically excluded
- Only rooms with `status=AVAILABLE` are returned
- Amenity filtering uses AND logic (room must have ALL specified amenities)
- Bed type and view filtering use OR logic (room matches ANY specified value)
- Availability checking excludes rooms with CONFIRMED bookings that overlap the requested dates
- Default sort is by price (ascending)
- Main image URL is fetched lazily for performance
