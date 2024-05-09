package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Exchange findById(long id);

    List<Exchange> findByStatusAndMember1IdOrStatusAndMember2Id(ExchangeStatus status, long member1Id, ExchangeStatus status2, long member2Id);
}
