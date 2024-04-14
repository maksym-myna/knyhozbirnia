package ua.lpnu.knyhozbirnia.mapper;

import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse;
import ua.lpnu.knyhozbirnia.model.InventoryItem;

@Service
public class InventoryItemMapper {
    public InventoryItemResponse toResponse(InventoryItem item) {
        return new InventoryItemResponse(item.getId(), item.getModifiedAt());
    }

    public InventoryItem toEntity(InventoryItemResponse itemResponse) {
        return InventoryItem.builder()
                .id(itemResponse.id())
                .modifiedAt(itemResponse.modifiedAt())
                .build();
    }
}
