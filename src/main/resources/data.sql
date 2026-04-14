INSERT INTO users (username, password, role)
SELECT 'librarian', 'librarian123', 'LIBRARIAN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'librarian'
);

INSERT INTO users (username, password, role)
SELECT 'member1@example.com', 'member123', 'MEMBER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE username = 'member1@example.com'
);
