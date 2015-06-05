package language;

import language.exception.FileNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import static java.nio.file.StandardWatchEventKinds.*;


@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String DICTIONARY_FILES_FOLDER = "./src/main/resources/dictionaryfiles";
    private static final String TEXT_FILE_FOLDER = "./src/main/resources/textfile";

    public static void main(String[] args) throws IOException, FileNotValidException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");

        String languageStr = language.determineLanguage(TEXT_FILE_FOLDER + "/TEXT.txt", DICTIONARY_FILES_FOLDER);
        LOGGER.info("Language is {}", languageStr);

        Path myDir = Paths.get(TEXT_FILE_FOLDER);

        LOGGER.info("Watching folder {}", TEXT_FILE_FOLDER);
        try {
            while (true) {
                WatchService watcher = myDir.getFileSystem().newWatchService();
                myDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

                WatchKey watckKey = watcher.take();

                List<WatchEvent<?>> events = watckKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == ENTRY_CREATE) {
                        LOGGER.info("File created: {}", event.context().toString());
                        String lang = language.determineLanguage(TEXT_FILE_FOLDER + "/" + event.context().toString(), DICTIONARY_FILES_FOLDER);
                        LOGGER.info("Language is {}", lang);
                    }
                    if (event.kind() == ENTRY_DELETE) {
                        LOGGER.info("File deleted: {}", event.context().toString());
                    }
                    if (event.kind() == ENTRY_MODIFY) {
                        LOGGER.info("File modified: {}", event.context().toString());
                        String lang = language.determineLanguage(TEXT_FILE_FOLDER + "/" + event.context().toString(), DICTIONARY_FILES_FOLDER);
                        LOGGER.info("Language is {}", lang);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }
    }
}
