package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.loan.LoanResponse;
import ua.lpnu.knyhozbirnia.service.LoanService;

@RestController
@RequestMapping("/loans/")
@AllArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @GetMapping("work/{work_id}/")
    public Slice<LoanResponse> getWorkLoans(
            @PathVariable("work_id") Integer workId,
            @PageableDefault(size = 1000) Pageable pageable) {
        return loanService.getWorkLoans(workId, pageable);
    }

    @GetMapping("item/{item_id}/")
    public Slice<LoanResponse> getItemsLoans(
            @PathVariable("item_id") Integer itemId,
            @PageableDefault(size = 1000) Pageable pageable) {
        return loanService.getItemLoans(itemId, pageable);
    }

    @GetMapping("{loan_id}/")
    public LoanResponse getLoan(@PathVariable("loan_id") Integer loanId) {
        return loanService.getLoan(loanId);
    }

    @PostMapping("{work_id}/")
    public LoanResponse loanWork(@PathVariable("work_id") Integer workId) {
        var loan = loanService.loanWork(workId);
        loanService.refreshCopies();
        return loan;
    }

    @PostMapping("{loan_id}/return/")
    public LoanResponse returnWork(@PathVariable("loan_id") Integer loanId) {
        var loan = loanService.returnWork(loanId);
        loanService.refreshCopies();
        return loan;
    }

    @DeleteMapping("{loan_id}/")
    public void deleteLoan(@PathVariable("loan_id") Integer loanId) {
        loanService.deleteLoan(loanId);
        loanService.refreshCopies();
    }

    @DeleteMapping("{loan_id}/return/")
    public void deleteReturn(@PathVariable("loan_id") Integer loanId) {
        loanService.deleteReturn(loanId);
        loanService.refreshCopies();
    }
}
