CREATE TABLE itinerary_notes (
    id BIGSERIAL PRIMARY KEY,
    itinerary_id BIGINT NOT NULL,
    note_url VARCHAR(512) NOT NULL,
    note_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_itinerary_notes_itinerary FOREIGN KEY (itinerary_id) REFERENCES itineraries(id) ON DELETE CASCADE
);

CREATE INDEX idx_itinerary_notes_itinerary_id ON itinerary_notes(itinerary_id);