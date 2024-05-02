package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    void deleteById(long id);
    User findByEmail(String email);
    List<User> findByRole(UserRole role);
}
