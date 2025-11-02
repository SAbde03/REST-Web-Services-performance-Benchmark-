package ma.projet.variantecspringmvc.repository;

import ma.projet.common.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
