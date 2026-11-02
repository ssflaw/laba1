-- пароль у всех: Password1
INSERT INTO users (username, email, password_hash, role, status) VALUES
    ('alice', 'alice@example.com', '$2a$10$WwpE9a9TETY8KsJVKKJAFeySyY308L9vzUP.GlQ7QsSkoK9I89l0a', 'STUDENT', 'ACTIVE'),
    ('bob', 'bob@example.com', '$2a$10$WwpE9a9TETY8KsJVKKJAFeySyY308L9vzUP.GlQ7QsSkoK9I89l0a', 'STUDENT', 'ACTIVE'),
    ('carol', 'carol@example.com', '$2a$10$WwpE9a9TETY8KsJVKKJAFeySyY308L9vzUP.GlQ7QsSkoK9I89l0a', 'STUDENT', 'BLOCKED'),
    ('teacher', 'teacher@example.com', '$2a$10$WwpE9a9TETY8KsJVKKJAFeySyY308L9vzUP.GlQ7QsSkoK9I89l0a', 'TEACHER', 'ACTIVE');

INSERT INTO exams (title, passing_score, time_limit_minutes, status) VALUES
    ('Java Basics', 3, 60, 'PUBLISHED'),
    ('SQL Basics', 2, 30, 'DRAFT');

INSERT INTO exam_tasks (exam_id, condition, reference_answer, max_score) VALUES
    (1, 'Ключевое слово для наследования класса?', 'extends', 2),
    (1, 'Сколько байт занимает int?', '4', 2),
    (2, 'Команда для выборки данных?', 'SELECT', 2);

INSERT INTO exam_attempts (exam_id, student_id, start_time, end_time, total_score, status) VALUES
    (1, 2, now() - INTERVAL '2 hours', now() - INTERVAL '90 minutes', 2, 'FAILED');

INSERT INTO task_solutions (attempt_id, task_id, answer, awarded_score) VALUES
    (1, 1, 'extends', 2),
    (1, 2, '8', 0);
