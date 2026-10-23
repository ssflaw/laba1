CREATE TABLE users (
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE CHECK (length(trim(username)) > 0),
    email    VARCHAR(255) UNIQUE
);

CREATE TABLE exams (
    id                 BIGSERIAL PRIMARY KEY,
    title              VARCHAR(255) NOT NULL CHECK (length(trim(title)) > 0),
    passing_score      INT NOT NULL CHECK (passing_score >= 0),
    time_limit_minutes INT NOT NULL CHECK (time_limit_minutes > 0),
    status             VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
                       CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED'))
);

CREATE TABLE exam_tasks (
    id               BIGSERIAL PRIMARY KEY,
    exam_id          BIGINT NOT NULL REFERENCES exams (id) ON DELETE CASCADE,
    condition        TEXT NOT NULL CHECK (length(trim(condition)) > 0),
    reference_answer TEXT NOT NULL,
    max_score        INT NOT NULL CHECK (max_score > 0)
);

CREATE TABLE exam_attempts (
    id          BIGSERIAL PRIMARY KEY,
    exam_id     BIGINT NOT NULL REFERENCES exams (id) ON DELETE CASCADE,
    student_id  BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP,
    total_score INT CHECK (total_score >= 0),
    status      VARCHAR(20) NOT NULL CHECK (status IN ('IN_PROGRESS', 'PASSED', 'FAILED')),
    CHECK ((status = 'IN_PROGRESS') = (end_time IS NULL)),
    CHECK (end_time IS NULL OR end_time >= start_time)
);

CREATE UNIQUE INDEX one_active_attempt_per_student
    ON exam_attempts (exam_id, student_id) WHERE status = 'IN_PROGRESS';

CREATE TABLE task_solutions (
    id            BIGSERIAL PRIMARY KEY,
    attempt_id    BIGINT NOT NULL REFERENCES exam_attempts (id) ON DELETE CASCADE,
    task_id       BIGINT NOT NULL REFERENCES exam_tasks (id) ON DELETE CASCADE,
    answer        TEXT NOT NULL,
    awarded_score INT NOT NULL CHECK (awarded_score >= 0),
    UNIQUE (attempt_id, task_id)
);
