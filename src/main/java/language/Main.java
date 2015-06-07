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

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");

        String defaultTextFileName = language.getTextFileName();
        String defaultTextFileFolder = language.getTextFileFolder();
        String defaultDictionaryFolder = language.getDictionaryFolder();

        boolean validUserInput = false;
        while (!validUserInput) {
            try {
                userInput(language, defaultTextFileName, defaultTextFileFolder, defaultDictionaryFolder);

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
                    validUserInput = true;
                }
            } catch (NoSuchFileException e) {
                LOGGER.error("Invalid folder", e);
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Error: ", e);
            }
        }
    }

    private static void userInput(Language language, String defaultTextFileName, String defaultTextFileFolder, String defaultDictionaryFolder) throws IOException {
        BufferedReader br;
        System.out.println("********************** USER INPUT **********************");
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter text file name (default is " + defaultTextFileName + "):");
        String textFileName = br.readLine();

        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter text file folder (default is " + defaultTextFileFolder + "):");
        String textFileFolder = br.readLine();

        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter dictionary folder (default is " + defaultDictionaryFolder + "):");
        String dictionaryFolder = br.readLine();

        language.setTextFileName(StringUtils.isBlank(textFileName) ? defaultTextFileName : textFileName);
        language.setTextFileFolder(StringUtils.isBlank(textFileFolder) ? defaultTextFileFolder : textFileFolder);
        language.setDictionaryFolder(StringUtils.isBlank(dictionaryFolder) ? defaultDictionaryFolder : dictionaryFolder);
        System.out.println("********************************************************");
    }
}
