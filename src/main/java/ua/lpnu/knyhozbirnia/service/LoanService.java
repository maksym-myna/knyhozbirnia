package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.loan.LoanResponse;
import ua.lpnu.knyhozbirnia.mapper.LoanMapper;
import ua.lpnu.knyhozbirnia.mapper.UserMapper;
import ua.lpnu.knyhozbirnia.model.InventoryItem;
import ua.lpnu.knyhozbirnia.model.Loan;
import ua.lpnu.knyhozbirnia.model.User;
import ua.lpnu.knyhozbirnia.repository.LoanRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final InventoryItemService itemService;
    private final UserService userService;
    private final AuthService authService;
    private final AsyncService asyncService;

    public Slice<LoanResponse> getUserLoans(Integer workId, Pageable pageable) {
        return loanRepository.getUserLoans(workId, pageable).map(loanMapper::setPfp);
    }

    public Slice<LoanResponse> getUserUnreturnedLoans(Integer workId, Pageable pageable) {
        return loanRepository.getUserUnreturnedLoans(workId, pageable).map(loanMapper::setPfp);
    }

    public Slice<LoanResponse> getUserReturnedLoans(Integer workId, Pageable pageable) {
        return loanRepository.getUserReturnedLoans(workId, pageable).map(loanMapper::setPfp);
    }
    public Slice<LoanResponse> getUserLoans(Pageable pageable) {
        return getUserLoans(userService.getCurrentUser().getId(), pageable);
    }

    public Slice<LoanResponse> getUserUnreturnedLoans(Pageable pageable) {
        return getUserUnreturnedLoans(userService.getCurrentUser().getId(), pageable);
    }

    public Slice<LoanResponse> getUserReturnedLoans(Pageable pageable) {
        return getUserReturnedLoans(userService.getCurrentUser().getId(), pageable);
    }

    public Slice<LoanResponse> getWorkLoans(Integer workId, Pageable pageable) {
        return loanRepository.getWorkLoans(workId, pageable);
    }

    public Slice<LoanResponse> getItemLoans(Integer itemId, Pageable pageable) {
        return loanRepository.getItemLoans(itemId, pageable);
    }

    public LoanResponse getLoan(Integer loanId) {
        return loanRepository.getLoan(loanId);
    }

    @Transactional
    @Modifying
    public LoanResponse loanWork(Integer workId) {
        User user = userService.getCurrentUser();
        InventoryItem item = itemService.getWorksAvailableItem(workId);
        Loan loan = Loan.builder().user(user).inventoryItem(item).build();
        Loan saved = loanRepository.save(loan);
        return loanMapper.toResponse(saved);
    }

    @Transactional
    @Modifying
    public LoanResponse returnWork(Integer loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();

        authService.checkEditAuthority(loan.getUser());

        loan.setReturnedAt(LocalDateTime.now());

        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toResponse(savedLoan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refreshCopies() {
        asyncService.refreshCopiesAsync();
    }

    @Transactional
    @Modifying
    public void deleteLoan(Integer loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();

        authService.checkEditAuthority(loan.getUser());

        loanRepository.deleteById(loanId);
    }

    @Transactional
    @Modifying
    public void deleteReturn(Integer loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();

        authService.checkEditAuthority(loan.getUser());

        loan.setReturnedAt(null);
        loanRepository.save(loan);
    }

}
