package site.ogobi.ogobi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OgobiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OgobiApplication.class, args);
    }

    @Bean(name = "uploadPath")
    public String uploadPath() {
        return "C:/summernoteimg";
    }
}
