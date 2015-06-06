package language;

import language.exception.FileNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@SpringBootApplication
@PropertySource("language.properties")
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, FileNotValidException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");
        LOGGER.info("**************************** WELCOME TO LANGUAGE DETECTOR ****************************");
        LOGGER.info("You can modify {} in {} manually", language.getTextFileName(), language.getTextFileFolder());
        LOGGER.info("and Language Detector will detect the language");
        LOGGER.info("based on existing dictionary files in {}", language.getDictionaryFolder());
        LOGGER.info("**************************************************************************************");

        String languageStr = language.determineLanguage();
        LOGGER.info("Language is {}", languageStr);

        Path myDir = Paths.get(language.getTextFileFolder());

        LOGGER.info("Watching folder {}", language.getTextFileFolder());
        try {
            while (true) {
                WatchService watcher = myDir.getFileSystem().newWatchService();
                myDir.register(watcher, ENTRY_MODIFY);

                WatchKey watckKey = watcher.take();

                List<WatchEvent<?>> events = watckKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == ENTRY_MODIFY) {
                        LOGGER.info("File modified: {}", event.context().toString());
                        String lang = language.determineLanguage();
                        LOGGER.info("Language is {}", lang);
                    }
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("Error: ", e);
        }
    }
}
