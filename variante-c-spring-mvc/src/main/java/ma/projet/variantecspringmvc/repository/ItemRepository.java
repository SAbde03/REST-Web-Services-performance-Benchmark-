package ma.projet.variantecspringmvc.repository;

import ma.projet.variantecspringmvc.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("select i from Item i join fetch i.category where i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithFetch(Long categoryId, Pageable pageable);
}
