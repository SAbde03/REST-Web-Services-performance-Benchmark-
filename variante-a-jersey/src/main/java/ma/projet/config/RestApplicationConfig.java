package ma.projet.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import ma.projet.resources.CategoryResource;
import ma.projet.resources.ItemResource;


import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/api")
public class RestApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(CategoryResource.class);
        classes.add(ItemResource.class);
        return classes;
    }

}
