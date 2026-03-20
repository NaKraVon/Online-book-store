INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Fiction', 'Classic fiction books', 0);
INSERT INTO categories (id, name, description, is_deleted) VALUES (2, 'Mystery', 'Detective and mystery stories', 0);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Test Book 1', 'Author 1', '111-111', 199.99, 'Description 1', 'image1.jpg', 0);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Test Book 2', 'Author 2', '222-222', 299.99, 'Description 2', 'image2.jpg', 0);

INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (3, 'Mystery Book', 'Author 3', '333-333', 399.99, 'Description 3', 'image3.jpg', 0);

INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 2);