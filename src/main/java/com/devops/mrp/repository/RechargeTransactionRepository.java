package com.devops.mrp.repository;

import com.devops.mrp.model.RechargeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RechargeTransactionRepository extends JpaRepository<RechargeTransaction, Long> {
    Optional<RechargeTransaction> findByTxnReference(String txnReference);

    List<RechargeTransaction> findAllByOrderByCreatedAtDesc();

    @Query("SELECT t FROM RechargeTransaction t WHERE " +
           "LOWER(t.mobileNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.operator) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.txnReference) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.status) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY t.createdAt DESC")
    List<RechargeTransaction> searchTransactions(@Param("query") String query);

    long countByStatus(String status);
}
