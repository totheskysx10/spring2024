INSERT INTO users (user_id, user_name, user_email, user_phone, user_show_contacts) VALUES (1, 'testUser1', 'test1@example.com', '89000000000', false);
INSERT INTO users (user_id, user_name, user_email, user_show_contacts) VALUES (2, 'testUser2', 'test2@example.com', false);
INSERT INTO users (user_id, user_name, user_email, user_show_contacts) VALUES (3, 'testUser3', 'test3@example.com', false);

INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year, book_rating) VALUES (1, 'Test Book 1', 'Test Author 1', 'ADVENTURE', '123', 2005, 0.0);
INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year, book_rating) VALUES (2, 'Test Book 2', 'Test Author 2', 'FICTION', '456', 2003, 0.0);
INSERT INTO books (book_id, book_title, book_author, book_genre, book_isbn, book_year, book_rating) VALUES (3, 'Test Book 3', 'Test Author 3', 'ART', '789', 2000, 0.0);


INSERT INTO user_books (user_id, book_id) VALUES (1, 1);
INSERT INTO user_books (user_id, book_id) VALUES (2, 3);
INSERT INTO user_books (user_id, book_id) VALUES (2, 2);
INSERT INTO user_books (user_id, book_id) VALUES (3, 2);

INSERT INTO user_offered_books (user_id, book_id) VALUES (1, 1);
INSERT INTO user_offered_books (user_id, book_id) VALUES (2, 3);
INSERT INTO user_offered_books (user_id, book_id) VALUES (2, 2);

INSERT INTO requests (request_sender, request_receiver, request_book_sender_wants, request_status, request_comment) VALUES (1, 2, 3, 'ACTUAL', '����� ���������� ���� ������.');
INSERT INTO requests (request_sender, request_receiver, request_book_sender_wants, request_status, request_comment) VALUES (3, 2, 3, 'ACTUAL', '���� �������� ���� ����� � ����� �� ���.');
INSERT INTO requests (request_sender, request_receiver, request_book_sender_wants, request_status, request_comment) VALUES (3, 2, 2, 'ACTUAL', '���� �������� ���� ����� � ����� �� ���.');
