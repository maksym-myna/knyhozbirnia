package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.knyhozbirnia.model.InventoryItem;
import ua.lpnu.knyhozbirnia.model.Work;
import ua.lpnu.knyhozbirnia.model.WorkMedium;
import ua.lpnu.knyhozbirnia.repository.InventoryItemRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class InventoryItemService {
    private final InventoryItemRepository itemRepository;
    public List<InventoryItem> addInventoryItems(Work work, WorkMedium medium, Integer quantity) {
        InventoryItem item = InventoryItem
                .builder()
                .medium(medium)
                .addedAt(LocalDateTime.now())
                .work(work)
                .build();
        List<InventoryItem> items = new ArrayList<>(Collections.nCopies(quantity, item));
        itemRepository.saveAll(items);
        return items;
    }
}
