package ua.lpnu.knyhozbirnia.dto.item;

import ua.lpnu.knyhozbirnia.model.InventoryItem;

public record InventoryItemResponse(Integer id) {
    public InventoryItemResponse(InventoryItem item) {
        this(item.getId());
    }
}
