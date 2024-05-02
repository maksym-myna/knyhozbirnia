package ua.lpnu.knyhozbirnia.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.loan.LoanResponse;
import ua.lpnu.knyhozbirnia.model.Loan;

@Service
@AllArgsConstructor
public class LoanMapper {
    InventoryItemMapper itemMapper;
    UserMapper userMapper;

    public LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .user(userMapper.toResponse(loan.getUser()))
                .title(loan.getInventoryItem().getWork().getTitle())
                .isbn(loan.getInventoryItem().getWork().getIsbn())
                .item(itemMapper.toResponse(loan.getInventoryItem()))
                .loanedAt(loan.getLoanedAt())
                .returnedAt(loan.getReturnedAt())
                .build();
    }

    public LoanResponse setPfp (LoanResponse loan) {
        userMapper.setPfp(loan.user());
        return loan;
    }
}
