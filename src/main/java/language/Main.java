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

    public static void main(String[] args) throws IOException, FileNotValidException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");

        String dictionaryFiles = "./src/main/resources/dictionaryfiles";
//        String languageStr = language.determineLanguage("./src/main/resources/textfile/TEXT.txt", dictionaryFiles);
//        LOGGER.debug("languageStr = " + languageStr);

        //define a folder root
        String path = "./src/main/resources/textfile";
        Path myDir = Paths.get(path);

        try {
            while (true) {
                WatchService watcher = myDir.getFileSystem().newWatchService();
                myDir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

                WatchKey watckKey = watcher.take();

                List<WatchEvent<?>> events = watckKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == ENTRY_CREATE) {
                        LOGGER.debug("Created: " + event.context().toString());
                        String lang = language.determineLanguage(path + "/" + event.context().toString(), dictionaryFiles);
                        LOGGER.debug("Language is {}", lang);
                    }
                    if (event.kind() == ENTRY_DELETE) {
                        LOGGER.debug("Delete: " + event.context().toString());
                    }
                    if (event.kind() == ENTRY_MODIFY) {
                        LOGGER.debug("Modify: " + event.context().toString());
                        String lang = language.determineLanguage(path + "/" + event.context().toString(), dictionaryFiles);
                        LOGGER.debug("Language is {}", lang);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
        }
    }
}
