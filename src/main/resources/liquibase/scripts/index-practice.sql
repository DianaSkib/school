-- liquibase formatted sql

-- changeset DianaSkib:1
CREATE INDEX idx_student_name ON student (name);

-- changeset DianaSkib:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color);