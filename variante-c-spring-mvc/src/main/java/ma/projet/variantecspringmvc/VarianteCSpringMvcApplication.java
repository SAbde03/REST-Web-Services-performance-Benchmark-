package ma.projet.variantecspringmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("ma.projet.variantecspringmvc.model")
public class VarianteCSpringMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(VarianteCSpringMvcApplication.class, args);
    }

}
