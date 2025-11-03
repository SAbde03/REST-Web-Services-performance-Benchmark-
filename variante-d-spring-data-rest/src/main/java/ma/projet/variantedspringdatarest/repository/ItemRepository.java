package ma.projet.variantedspringdatarest.repository;

import ma.projet.variantedspringdatarest.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "items", path = "items")
public interface ItemRepository extends JpaRepository<Item, Long> {

    @RestResource(path = "byCategory", rel = "byCategory")
    Page<Item> findByCategoryId(Long categoryId, Pageable pageable);

    @Query("select i from Item i join fetch i.category where i.category.id = :categoryId")
    Page<Item> findByCategoryIdWithFetch(Long categoryId, Pageable pageable);
}
