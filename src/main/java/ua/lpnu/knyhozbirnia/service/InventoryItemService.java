package ua.lpnu.knyhozbirnia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse;
import ua.lpnu.knyhozbirnia.mapper.InventoryItemMapper;
import ua.lpnu.knyhozbirnia.model.InventoryItem;
import ua.lpnu.knyhozbirnia.model.Work;
import ua.lpnu.knyhozbirnia.repository.InventoryItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class InventoryItemService {
    private final InventoryItemRepository itemRepository;
    private final InventoryItemMapper itemMapper;

    public Slice<InventoryItemResponse> getAllItems(Integer workId, Pageable pageable) {
        return itemRepository.findAllByWorkId(workId, pageable);
    }

    public InventoryItem getWorksAvailableItem(Integer id) {
        return itemRepository.getWorksAvailableItem(id, Pageable.ofSize(1)).stream().findFirst().orElseThrow();
    }


    public InventoryItemResponse getItem(Integer itemId) {
        return itemRepository.findItemById(itemId);
    }

    @Transactional
    @Modifying
    public Page<InventoryItemResponse> addItems(Integer workId, Integer quantity) {
        Work workStub = Work.builder().id(workId).build();
        return new PageImpl<>(addItems(workStub, quantity));
    }

    @Transactional
    @Modifying
    public List<InventoryItemResponse> addItems(Work work, Integer quantity) {
        InventoryItem item = InventoryItem
                .builder()
//                .modifiedAt(LocalDateTime.now())
                .work(work)
                .build();

        List<InventoryItem> items = new ArrayList<>(Collections.nCopies(quantity, item));
        itemRepository.saveAll(items);
        return items.stream().map(itemMapper::toResponse).toList();
    }

    @Transactional
    @Modifying
    public void deleteItem(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}
