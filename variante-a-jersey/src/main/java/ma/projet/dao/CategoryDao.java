package ma.projet.dao;

import jakarta.persistence.EntityTransaction;
import ma.projet.persistence.PersistenceManager;
import jakarta.persistence.EntityManager;
import ma.projet.common.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public class CategoryDao {

    //GET /categories?page=&size= : liste paginée
    public Page<Category> findAll(Pageable pageable) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try{
            List<Category> categories = em.createQuery("select c from Category c", Category.class)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            long total = (long) em.createQuery("select count(c) from Category c").getSingleResult();

            return new PageImpl<>(categories, pageable, total);
        }finally{
            em.close();
        }
    }

    //GET /categories/{id}
    public Optional<Category> findById(long id) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        try {
            Category category = em.find(Category.class, id);
            return Optional.ofNullable(category);
        } finally {
            em.close();
        }
    }

    //POST /categories
    //PUT /categories/{id}
    public Category save(Category category) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Category categorySaved = em.merge(category);
            tx.commit();
            return categorySaved;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw e;
        }finally{
            em.close();
        }
    }

    //DELETE /categories/{id}
    public void deleteById(long id) {
        EntityManager em = PersistenceManager.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try{
            tx.begin();
            Category category = em.find(Category.class, id);
            if(category != null){
                em.remove(category);
            }
            tx.commit();
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw e;
        }finally{
            em.close();
        }
    }

    public boolean existsById(long id) {
        return findById(id).isPresent();
    }
}
