package language;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class LanguageReader {

    public static final String ALLOWED_CHARS_REGEX = "[^a-zA-Z ]";
    public static final String ALLOWED_PUNCTUATIONS = ".,;:";
    private Map<String, Set<String>> dictionary = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageReader.class);


    public void readAndStore(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        String fileName = StringUtils.substringBefore(path.getFileName().toString(), ".");
        LOGGER.debug("fileName {}", fileName);
        for (String line : lines) {
            LOGGER.debug("line = {}", line);
            lineToDictionary(line, fileName);
        }
    }

    public void lineToDictionary(String line, String language) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalStateException();
        }
        line = line.replaceAll(LanguageReader.ALLOWED_CHARS_REGEX, "");

        String[] wordArray = StringUtils.split(line);
        Set<String> newWords = new HashSet<>(Arrays.asList(wordArray));

        Set<String> currentWords = dictionary.get(language);
        if (currentWords == null) {
            dictionary.put(language, newWords);
        } else {
            currentWords.addAll(newWords);
        }

        LOGGER.debug("dictionary {}", dictionary);
    }

    public Map<String, Set<String>> getDictionary() {
        return dictionary;
    }
}
