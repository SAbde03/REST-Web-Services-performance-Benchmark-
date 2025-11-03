package ma.projet.variantedspringdatarest.config;

import ma.projet.variantedspringdatarest.model.Category;
import ma.projet.variantedspringdatarest.model.Item;
import ma.projet.variantedspringdatarest.repository.CategoryRepository;
import ma.projet.variantedspringdatarest.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class DataInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Ce Bean ne sera activé que si le profil "init-data" est actif.
     * Cela évite de re-peupler la DB à chaque fois que vous lancez l'app.
     */
    @Bean
    @Profile("init-data")
    public CommandLineRunner loadData() {
        return args -> {
            System.out.println("==========================================");
            System.out.println("DÉBUT DU PEUPLEMENT DE LA BASE DE DONNÉES");
            System.out.println("==========================================");

            // Étape 1 : S'assurer que la base est vide
            if (categoryRepository.count() > 0 || itemRepository.count() > 0) {
                System.out.println("La base de données contient déjà des données. Annulation.");
                return;
            }


            System.out.println("Création de 2 000 catégories...");
            List<Category> categories = new ArrayList<>();
            for (int i = 1; i <= 2000; i++) {
                Category cat = new Category();
                cat.setCode(String.format("CAT%04d", i)); // CAT0001, CAT0002...
                cat.setName("Category " + i);
                categories.add(cat);
            }
            // Sauvegarde par lot (batch)
            categoryRepository.saveAll(categories);
            System.out.println("Catégories créées.");


            System.out.println("Création de 100 000 items (50 par catégorie)...");
            List<Item> itemsBatch = new ArrayList<>();

            for (Category cat : categories) {
                for (int j = 1; j <= 50; j++) {
                    Item item = new Item();
                    item.setSku(String.format("SKU-%s-%03d", cat.getCode(), j));
                    item.setName("Item " + j + " for " + cat.getName());
                    item.setPrice(BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(10.0, 500.0)));
                    item.setStock(ThreadLocalRandom.current().nextInt(0, 1000));
                    item.setCategory(cat);
                    itemsBatch.add(item);

                    // Insère par lots de 1000 pour ne pas saturer la mémoire
                    if (itemsBatch.size() >= 1000) {
                        itemRepository.saveAll(itemsBatch);
                        itemsBatch.clear();
                    }
                }
            }
            // Sauvegarde le dernier lot s'il en reste
            if (!itemsBatch.isEmpty()) {
                itemRepository.saveAll(itemsBatch);
            }

            System.out.println("Items créés.");
            System.out.println("==========================================");
            System.out.println("PEUPLEMENT TERMINÉ.");
            System.out.println("==========================================");
        };
    }
}