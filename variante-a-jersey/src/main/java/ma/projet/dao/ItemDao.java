package ma.projet.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.projet.model.Item;
import ma.projet.persistence.PersistenceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class ItemDao {

    //GET /items?page=&size=
    public Page<Item> findAll(Pageable pageable) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try {
            List<Item> items = em.createQuery("SELECT i FROM Item i", Item.class)
                    .setFirstResult((int) pageable.getOffset())
                    .setFirstResult(pageable.getPageSize())
                    .getResultList();

            long total = (long) em.createQuery("SELECT COUNT(i) FROM Item i").getSingleResult();

            return new PageImpl<>(items, pageable, total);
        } finally {
            em.close();
        }
    }

    //GET /items/{id}
    public Optional<Item> findById(long id) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try {
            Item item = em.find(Item.class, id);
            return Optional.ofNullable(item);
        } finally {
            em.close();
        }
    }

    /*
     POST /items
     PUT /items/{id}
     */
    public Item save(Item item) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Item mergedItem = em.merge(item);
            tx.commit();
            return mergedItem;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    //DELETE /items/{id}
    public void deleteById(Long id) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Item item = em.find(Item.class, id);
            if (item != null) {
                em.remove(item);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    //GET /items?categoryId=... (Mode Baseline)
    public Page<Item> findByCategory(Long categoryId, Pageable pageable) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try {
            List<Item> items = em.createQuery("select i from Item i where i.category.id= :categoryId", Item.class)
                    .setParameter("categoryId", categoryId)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            long total = (long) em.createQuery("select count(i) from Item i where i.category.id= :categoryId")
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
            return new PageImpl<>(items, pageable, total);
        } finally {
            em.close();
        }
    }

    //GET /items?categoryId=... (Mode JOIN FETCH)
    public Page<Item> findByCategoryIdWithFetch(Long categoryId, Pageable pageable) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try {
            List<Item> items = em.createQuery("select i from Item i join fetch i.category where i.category.id= :categoryId", Item.class)
                    .setParameter("categoryId", categoryId)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            long total = (long) em.createQuery("SELECT COUNT(i) FROM Item i WHERE i.category.id= :categoryId")
                    .setParameter("categoryId", categoryId)
                    .getSingleResult();
            return new PageImpl<>(items, pageable, total);
        } finally {
            em.close();
        }
    }

}
