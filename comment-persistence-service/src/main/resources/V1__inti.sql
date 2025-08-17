-- V1__init.sql

CREATE TABLE IF NOT EXISTS pull_requests (
    id BIGSERIAL PRIMARY KEY,
    owner TEXT NOT NULL,
    repo  TEXT NOT NULL,
    number INT NOT NULL,
    unresolved_count INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_pull_requests_owner_repo_number UNIQUE (owner, repo, number)
);

CREATE TABLE IF NOT EXISTS review_threads (
    id BIGSERIAL PRIMARY KEY,
    pull_request_id BIGINT NOT NULL REFERENCES pull_requests(id) ON DELETE CASCADE,
    resolved_by TEXT,
    resolved_at TIMESTAMPTZ,
    resolved BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    author TEXT NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    path TEXT,
    line INT,
    body TEXT NOT NULL,
    thread_id BIGINT REFERENCES review_threads(id) ON DELETE SET NULL,
    pull_request_id BIGINT NOT NULL REFERENCES pull_requests(id) ON DELETE CASCADE
);
