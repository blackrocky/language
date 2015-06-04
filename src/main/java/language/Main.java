package language;

import language.exception.FileNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, FileNotValidException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        LanguageFileReader languageFileReader = (LanguageFileReader) ctx.getBean("languageFileReader");
        Dictionary dictionary = (Dictionary) ctx.getBean("dictionary");
        Language language = (Language) ctx.getBean("language");

        List<LanguageFile> dictionaryFiles = languageFileReader.readDirectory("./src/main/resources/languagefiles");
        for (LanguageFile file : dictionaryFiles) {
            try {
                dictionary.readAndStore(file.getParent() + "/" + file.getFileName()); // TODO find better way
            } catch (FileNotValidException e) {
                continue;
            }
        }

        String languageStr = language.determineLanguage("./src/main/resources/textfile/TEXT.txt", dictionaryFiles);
        LOGGER.debug("languageStr = " + languageStr);
    }
}
