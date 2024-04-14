package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
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
    private final UserMapper userMapper;
    private final AuthService authService;

    public Slice<LoanResponse> getUserLoans(Integer workId, Pageable pageable) {
        return loanRepository
                .getUserLoans(workId, pageable)
                .map(loan -> {
                    userMapper.setPfp(loan.user());
                    return loan;}
                );
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
        return loanMapper.toResponse(loanRepository.save(loan));
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
