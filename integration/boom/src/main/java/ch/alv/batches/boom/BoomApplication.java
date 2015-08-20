package ch.alv.batches.boom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        "ch.alv.batches.company.to.master, " +
        "ch.alv.batches.partnerjob.to.master, " +
        "ch.alv.batches.commons.web")
public class BoomApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BoomApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}

