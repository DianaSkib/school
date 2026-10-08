-- 1. Получить всех студентов, возраст которых между 10 и 20
SELECT * FROM student WHERE age BETWEEN 10 AND 20;

-- 2. Получить только имена всех студентов
SELECT name FROM student;

-- 3. Получить всех студентов, у которых в имени есть буква 'о'
SELECT * FROM student WHERE name LIKE '%о%';

-- 4. Получить всех студентов, у которых возраст меньше id
SELECT * FROM student WHERE age < id;

-- 5. Получить всех студентов, упорядоченных по возрасту (по возрастанию)
SELECT * FROM student ORDER BY age;