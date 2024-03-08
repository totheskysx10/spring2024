package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findById(long id);
    List<Book> findByGenreIgnoreCase(BookGenre genre);
    List<Book> findByTitleAndAuthorIgnoreCase(String title, String author);
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    @Query("SELECT b FROM Book b WHERE SIZE(b.usersOfferingForExchange) > 0")
    List<Book> findBooksWithUsersOfferingForExchange();
}
