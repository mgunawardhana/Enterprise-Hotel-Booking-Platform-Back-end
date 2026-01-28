-- Migration: Create bookings table for room availability tracking
-- Version: V3
-- Description: Adds bookings table to support availability filtering in room search

CREATE TABLE IF NOT EXISTS bookings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_room FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_booking_room_id ON bookings(room_id);
CREATE INDEX IF NOT EXISTS idx_booking_dates ON bookings(check_in_date, check_out_date);
CREATE INDEX IF NOT EXISTS idx_booking_status ON bookings(status);

-- Add comment
COMMENT ON TABLE bookings IS 'Stores room booking information for availability checking';
COMMENT ON COLUMN bookings.status IS 'Booking status: CONFIRMED, CANCELLED, PENDING, etc.';
