INSERT INTO users (user_id, user_name, user_email, user_phone) VALUES (1, 'testUser1', 'test1@example.com', '89000000000');
INSERT INTO users (user_id, user_name, user_email) VALUES (2, 'testUser2', 'test2@example.com');
INSERT INTO users (user_id, user_name, user_email) VALUES (3, 'testUser3', 'test3@example.com');

INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year) VALUES (1, 'Test Book 1', 'Test Author 1', 'ADVENTURE', '123', 2005);
INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year) VALUES (2, 'Test Book 2', 'Test Author 2', 'FICTION', '456', 2003);
INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year) VALUES (3, 'Test Book 3', 'Test Author 3', 'ART', '789', 2000);


INSERT INTO user_books (user_id, book_id) VALUES (1, 1);
INSERT INTO user_books (user_id, book_id) VALUES (2, 2);
INSERT INTO user_books (user_id, book_id) VALUES (2, 3);

INSERT INTO user_offered_books (user_id, book_id) VALUES (1, 1);
INSERT INTO user_offered_books (user_id, book_id) VALUES (2, 2);

INSERT INTO exchanges (member1, member2, book1, book2, received1, received2) VALUES (1, 2, 1, 2, false, false);
INSERT INTO exchanges (member1, member2, book1, book2, received1, received2) VALUES (1, 2, 2, 3, false, false);
