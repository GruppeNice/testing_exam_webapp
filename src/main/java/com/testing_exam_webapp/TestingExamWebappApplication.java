package com.testing_exam_webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.awt.Desktop;
import java.net.URI;
import java.util.Locale;

@SpringBootApplication()
public class TestingExamWebappApplication {

    private static final Logger logger = LoggerFactory.getLogger(TestingExamWebappApplication.class);

    public static void main(final String[] args) {
        SpringApplication.run(TestingExamWebappApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            try {
                // Wait a moment for the server to fully start
                Thread.sleep(2000);

                final String swaggerUrl = "http://localhost:8080/swagger-ui.html";

                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(swaggerUrl));
                    logger.info("\n✅ Swagger UI opened in your browser!");
                } else {
                    // Fallback for headless environments
                    final String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
                    final Runtime runtime = Runtime.getRuntime();

                    if (os.contains("mac")) {
                        runtime.exec("open " + swaggerUrl);
                    } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                        runtime.exec("xdg-open " + swaggerUrl);
                    }
                    logger.info("\n✅ Swagger UI opened in your browser!");
                }

                logger.info("   URL: {}", swaggerUrl);
            } catch (java.net.URISyntaxException | java.io.IOException | InterruptedException e) {
                logger.warn("\n⚠️  Could not open browser automatically.");
                logger.warn("   Please open manually: http://localhost:8080/swagger-ui.html");
            }
        };
    }

}
