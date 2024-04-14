package ua.lpnu.knyhozbirnia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse;
import ua.lpnu.knyhozbirnia.service.InventoryItemService;

@AllArgsConstructor
@RestController
@RequestMapping("/works/items/")
public class InventoryItemController {
    private final InventoryItemService itemService;

    @GetMapping("{workId}/")
    public Slice<InventoryItemResponse> getInventoryItems(
            @PathVariable Integer workId,
            @PageableDefault(size = 25) Pageable pageable){
        return itemService.getAllItems(workId, pageable);
    }

    @GetMapping("{itemId}/")
    public InventoryItemResponse getInventoryItem(@PathVariable Integer itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping("{workId}/")
    public Page<InventoryItemResponse> addInventoryItem(@PathVariable Integer workId, @RequestBody Integer quantity) {
        return itemService.addItems(workId, quantity);
    }

    @DeleteMapping("{itemId}/")
    public void deleteInventoryItem(@PathVariable Integer itemId) {
        itemService.deleteItem(itemId);
    }
}
