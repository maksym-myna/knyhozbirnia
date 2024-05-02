package ua.lpnu.knyhozbirnia.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse;
import ua.lpnu.knyhozbirnia.model.InventoryItem;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
    String SELECT_ITEMS_QUERY = """
        SELECT new ua.lpnu.knyhozbirnia.dto.item.InventoryItemResponse(ii.id)
        FROM InventoryItem ii
    """;
    String FILTER_BY_WORK_ID_CLAUSE = "WHERE ii.work.id = :workId";
    String SELECT_WORKS_ITEMS_QUERY = SELECT_ITEMS_QUERY + ' ' + FILTER_BY_WORK_ID_CLAUSE;
    String SELECT_ITEM_QUERY = SELECT_ITEMS_QUERY + " WHERE ii.id = :id";
    String SELECT_AVAILABLE_ITEM_QUERY = "SELECT ii from FROM InventoryItem ii LEFT JOIN Loan l on ii.id = l.inventoryItem.id " + FILTER_BY_WORK_ID_CLAUSE +
            " AND (ii.work.medium = 'EBOOK' OR (l.loanedAt IS NULL AND l.returnedAt IS NULL) OR (l.loanedAt IS NOT NULL AND l.returnedAt IS NOT NULL))";

    @Query(SELECT_WORKS_ITEMS_QUERY)
    Slice<InventoryItemResponse> findAllByWorkId(@Param("workId") Integer workId, Pageable pageable);

    @Query(SELECT_ITEM_QUERY)
    InventoryItemResponse findItemById(@Param("id") Integer id);

    @Query("""
        SELECT
            ii
        FROM
            InventoryItem ii
        LEFT JOIN
            Loan l on ii.id = l.inventoryItem.id
        WHERE
            l.inventoryItem.work.id = :workId AND (
        (l.loanedAt = (
            SELECT MAX(l2.loanedAt) FROM Loan l2 WHERE l2.inventoryItem.id = l.inventoryItem.id
        ) AND l.returnedAt IS NOT NULL) OR l.loanedAt IS NULL)
    """)
    Slice<InventoryItem> getWorksAvailableItem(@Param("workId") Integer workId, Pageable pageable);
}
