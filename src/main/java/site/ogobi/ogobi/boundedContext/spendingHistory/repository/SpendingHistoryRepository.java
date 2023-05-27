package site.ogobi.ogobi.boundedContext.spendingHistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.ogobi.ogobi.boundedContext.spendingHistory.entity.SpendingHistory;

@Repository
public interface SpendingHistoryRepository extends JpaRepository<SpendingHistory, Long> {
}
