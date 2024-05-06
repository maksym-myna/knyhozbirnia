package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.loan.LoanResponse;
import ua.lpnu.knyhozbirnia.model.Loan;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {
    String SELECT_LOAN_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.loan.LoanResponse(
                l.id,
                l.user,
                l.inventoryItem.work.title,
                l.inventoryItem.work.isbn,
                l.inventoryItem,
                l.loanedAt,
                l.returnedAt
            ) FROM Loan l
        """;

    String SELECT_LOANS_BY_USER_ID_QUERY = SELECT_LOAN_QUERY + " WHERE l.user.id = :userId";
    String SELECT_RETURNED_LOANS_BY_USER_ID_QUERY = SELECT_LOANS_BY_USER_ID_QUERY + " AND l.returnedAt IS NOT NULL";
    String SELECT_UNRETURNED_LOANS_BY_USER_ID_QUERY = SELECT_LOANS_BY_USER_ID_QUERY + " AND l.returnedAt IS NULL";
    String SELECT_LOANS_BY_ITEM_ID_QUERY = SELECT_LOAN_QUERY + " WHERE l.inventoryItem.id = :itemId";
    String SELECT_LOANS_BY_WORK_ID_QUERY = SELECT_LOAN_QUERY + " WHERE l.inventoryItem.work.id = :workId";
    String SELECT_LOANS_BY_LOAN_ID_QUERY = SELECT_LOAN_QUERY + " WHERE l.id = :loanId";

    @Query(value = SELECT_LOANS_BY_USER_ID_QUERY)
    Slice<LoanResponse> getUserLoans(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = SELECT_RETURNED_LOANS_BY_USER_ID_QUERY)
    Slice<LoanResponse> getUserReturnedLoans(@Param("userId") Integer userId, Pageable pageable);
    @Query(value = SELECT_UNRETURNED_LOANS_BY_USER_ID_QUERY)
    Slice<LoanResponse> getUserUnreturnedLoans(@Param("userId") Integer userId, Pageable pageable);

    @Query(value = SELECT_LOANS_BY_WORK_ID_QUERY)
    Slice<LoanResponse> getWorkLoans(@Param("workId") Integer workId, Pageable pageable);
    @Query(value = SELECT_LOANS_BY_ITEM_ID_QUERY)
    Slice<LoanResponse> getItemLoans(@Param("itemId") Integer itemId, Pageable pageable);
    @Query("""
        SELECT new ua.lpnu.knyhozbirnia.dto.loan.LoanResponse(
                l.id,
                l.user,
                l.inventoryItem.work.title,
                l.inventoryItem.work.isbn,
                l.inventoryItem,
                l.loanedAt,
                l.returnedAt
            ) FROM Loan l
        """)
    LoanResponse getLoan(@Param("loanId") Integer loanId);

    @Procedure("refresh_available_copies_efficiently")
    void refreshAvailableCopies();
}
