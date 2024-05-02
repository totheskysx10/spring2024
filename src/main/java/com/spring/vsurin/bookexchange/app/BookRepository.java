package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findById(long id);
    void deleteById(long id);
    Page<Book> findByGenre(BookGenre genre, Pageable pageable);
    List<Book> findByTitleAndAuthorIgnoreCase(String title, String author);
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE SIZE(b.usersOfferingForExchange) > 0")
    Page<Book> findBooksWithUsersOfferingForExchange(Pageable pageable);

    BookCoverProjection findCoverImageById(long bookId);
}
