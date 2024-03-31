package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.model.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {}
