package language;

import language.exception.FileNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, FileNotValidException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        Language language = (Language) ctx.getBean("language");

        String languageStr = language.determineLanguage("./src/main/resources/textfile/TEXT.txt", "./src/main/resources/dictionaryfiles");
        LOGGER.debug("languageStr = " + languageStr);
    }
}
