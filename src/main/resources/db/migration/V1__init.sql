CREATE TABLE child_devices (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(100) NOT NULL UNIQUE,
    label VARCHAR(120) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE keystroke_data (
    id BIGSERIAL PRIMARY KEY,
    child_device_id BIGINT NOT NULL REFERENCES child_devices(id),
    payload TEXT NOT NULL,
    captured_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_keystroke_child_captured ON keystroke_data(child_device_id, captured_at DESC);

CREATE TABLE app_usage_data (
    id BIGSERIAL PRIMARY KEY,
    child_device_id BIGINT NOT NULL REFERENCES child_devices(id),
    payload TEXT NOT NULL,
    captured_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_app_usage_child_captured ON app_usage_data(child_device_id, captured_at DESC);

CREATE TABLE location_data (
    id BIGSERIAL PRIMARY KEY,
    child_device_id BIGINT NOT NULL REFERENCES child_devices(id),
    latitude NUMERIC(10,7) NOT NULL,
    longitude NUMERIC(10,7) NOT NULL,
    captured_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_location_child_captured ON location_data(child_device_id, captured_at DESC);
