package com.testing_exam_webapp;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        Neo4jAutoConfiguration.class,
        Neo4jDataAutoConfiguration.class
})
public class TestingExamWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingExamWebappApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try {
                // Wait a moment for the server to fully start
                Thread.sleep(2000);

                String swaggerUrl = "http://localhost:8080/swagger-ui.html";

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(swaggerUrl));
                    System.out.println("\n✅ Swagger UI opened in your browser!");
                } else {
                    // Fallback for headless environments
                    String os = System.getProperty("os.name").toLowerCase();
                    Runtime runtime = Runtime.getRuntime();

                    if (os.contains("win")) {
                        // runtime.exec("cmd /c start " + swaggerUrl);
                    } else if (os.contains("mac")) {
                        runtime.exec("open " + swaggerUrl);
                    } else {
                        runtime.exec("xdg-open " + swaggerUrl);
                    }
                    System.out.println("\n✅ Swagger UI opened in your browser!");
                }

                System.out.println("   URL: " + swaggerUrl);
            } catch (Exception e) {
                System.out.println("\n⚠️  Could not open browser automatically.");
                System.out.println("   Please open manually: http://localhost:8080/swagger-ui.html");
            }
        };
    }

}
