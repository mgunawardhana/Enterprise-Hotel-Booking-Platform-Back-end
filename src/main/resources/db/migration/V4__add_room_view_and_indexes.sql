-- Migration: Add view column and additional indexes to rooms table
-- Version: V4
-- Description: Adds view column for room view filtering and optimizes search performance

-- Add view column to rooms table
ALTER TABLE rooms ADD COLUMN IF NOT EXISTS view VARCHAR(20);

-- Create additional indexes for search optimization
CREATE INDEX IF NOT EXISTS idx_room_max_guests ON rooms(max_guests);
CREATE INDEX IF NOT EXISTS idx_room_view ON rooms(view);

-- Add comments
COMMENT ON COLUMN rooms.view IS 'Room view type: OCEAN_VIEW, GARDEN_VIEW, PANORAMIC_VIEW';

-- Note: The following indexes should already exist from initial migration:
-- idx_room_price (price_per_night)
-- idx_room_rating (rating)
-- idx_room_status (status)
-- idx_room_deleted (is_deleted)
