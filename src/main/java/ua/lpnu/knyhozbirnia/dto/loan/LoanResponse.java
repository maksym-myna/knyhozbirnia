package ua.lpnu.knyhozbirnia.dto.loan;

import lombok.Builder;
import ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse;
import ua.lpnu.knyhozbirnia.dto.user.UserResponse;
import ua.lpnu.knyhozbirnia.model.InventoryItem;
import ua.lpnu.knyhozbirnia.model.User;

import java.time.LocalDateTime;

@Builder
public record LoanResponse(
        int id,
        UserResponse user,
        String title,
        String isbn,
        InventoryItemResponse item,
        LocalDateTime loanedAt,
        LocalDateTime returnedAt
) {
    public LoanResponse (int id, User user, String title, String isbn, InventoryItem item, LocalDateTime loanedAt, LocalDateTime returnedAt) {
        this(id, new UserResponse(user), title, isbn, new InventoryItemResponse(item), loanedAt, returnedAt);
    }
}
