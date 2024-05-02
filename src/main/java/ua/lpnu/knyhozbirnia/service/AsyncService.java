package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.repository.LoanRepository;

@Service
@AllArgsConstructor
public class AsyncService {
    private final LoanRepository loanRepository;

    @Async
    public void refreshCopiesAsync() {
        loanRepository.refreshAvailableCopies();
    }
}
