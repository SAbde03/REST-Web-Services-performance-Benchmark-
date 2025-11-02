package ma.projet.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class PersistenceManager {

    private static final String PERSISTENCE_UNIT_NAME = "perf-unit";
    private static EntityManagerFactory emf;

    static{
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation de l'EntityManagerFactory",e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
