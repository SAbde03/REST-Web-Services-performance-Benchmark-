package ma.projet.variantecspringmvc.service;


import ma.projet.variantecspringmvc.model.Item;
import ma.projet.variantecspringmvc.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return itemRepository.existsById(id);
    }

    /**
     * Gère la logique de l'endpoint GET /items?categoryId=...
     * @param categoryId L'ID de la catégorie à filtrer.
     * @param useJoinFetch Un flag pour basculer entre le mode baseline et le mode optimisé.
     * @param pageable La pagination.
     * @return Une page d'Items.
     */
    public Page<Item> findByCategoryId(Long categoryId, boolean useJoinFetch, Pageable pageable) {
        if (useJoinFetch) {
            return itemRepository.findByCategoryIdWithFetch(categoryId, pageable);
        } else {
            return itemRepository.findByCategoryId(categoryId, pageable);
        }
    }
}
