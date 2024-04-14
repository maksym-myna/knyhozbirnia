package ua.lpnu.knyhozbirnia.dto.item;

import ua.lpnu.knyhozbirnia.model.InventoryItem;

import java.time.LocalDateTime;

public record InventoryItemResponse(Integer id, LocalDateTime modifiedAt) {
    public InventoryItemResponse(InventoryItem item) {
        this(item.getId(), item.getModifiedAt());
    }
}
