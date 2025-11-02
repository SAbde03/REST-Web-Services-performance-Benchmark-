package ma.projet.variantecspringmvc.repository;

import ma.projet.common.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
