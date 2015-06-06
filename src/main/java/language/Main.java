package language;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)  {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");

        try {
            userInput(language);

            LOGGER.info("**************************** WELCOME TO LANGUAGE DETECTOR ****************************");
            LOGGER.info("You can modify {} in {} manually", language.getTextFileName(), language.getTextFileFolder());
            LOGGER.info("and Language Detector will detect the language");
            LOGGER.info("based on existing dictionary files in {}", language.getDictionaryFolder());
            LOGGER.info("**************************************************************************************");

            String languageStr = language.determineLanguage();
            LOGGER.info("Language is {}", languageStr);
            Path myDir = Paths.get(language.getTextFileFolder());

            LOGGER.info("Watching folder {}", language.getTextFileFolder());

            while (true) {
                WatchService watcher = myDir.getFileSystem().newWatchService();
                myDir.register(watcher, ENTRY_MODIFY);

                WatchKey watchKey = watcher.take();

                List<WatchEvent<?>> events = watchKey.pollEvents();
                for (WatchEvent event : events) {
                    if (event.kind() == ENTRY_MODIFY) {
                        LOGGER.info("File modified: {}", event.context().toString());
                        String lang = language.determineLanguage();
                        LOGGER.info("Language is {}", lang);
                    }
                }
            }
        } catch (NoSuchFileException e) {
            LOGGER.error("Invalid folder", e);
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Error: ", e);
        }
    }

    private static void userInput(Language language) throws IOException {
        BufferedReader br = null;
        try {
            System.out.println("********************** USER INPUT \"**********************");
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter text file name (default is " + language.getTextFileName() + "):");
            String textFileName = br.readLine();

            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter text file folder (default is " + language.getTextFileFolder() + "):");
            String textFileFolder = br.readLine();

            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter dictionary folder (default is " + language.getDictionaryFolder() + "):");
            String dictionaryFolder = br.readLine();

            if (StringUtils.isNotBlank(textFileName)) language.setTextFileName(textFileName);
            if (StringUtils.isNotBlank(textFileFolder)) language.setTextFileFolder(textFileFolder);
            if (StringUtils.isNotBlank(dictionaryFolder)) language.setDictionaryFolder(dictionaryFolder);
        } finally {
            if (br != null) br.close();
        }
    }
}
