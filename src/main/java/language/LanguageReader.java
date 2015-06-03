package language;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageReader {
    public static final String REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";
    public static final String CHAR_TO_GET_REGEX = "[^a-zA-Z ]";
    private Map<String, Set<String>> dictionary = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageReader.class);


    public void readAndStore(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        LOGGER.debug("Reading file {}", path.getFileName().toString());

        String language = StringUtils.substringBefore(path.getFileName().toString(), ".");
        Pattern pattern = Pattern.compile(REGEX);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            LOGGER.trace("line = {}", line);
            LOGGER.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line)) {
                if (!matcher.matches()) {
                    throw new IllegalStateException("File " + pathStr + " contains illegal characters");
                }
                lineToDictionary(line, language);
            }
        }
    }

    public void lineToDictionary(String line, String language) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalStateException();
        }
        line = StringUtils.lowerCase(line.replaceAll(LanguageReader.CHAR_TO_GET_REGEX, ""));

        String[] wordArray = StringUtils.split(line);
        Set<String> newWords = new HashSet<>(Arrays.asList(wordArray));

        Set<String> currentWords = dictionary.get(language);
        if (currentWords == null) {
            dictionary.put(language, newWords);
        } else {
            currentWords.addAll(newWords);
        }

        LOGGER.trace("dictionary {}", dictionary);
    }

    public Map<String, Set<String>> getDictionary() {
        return dictionary;
    }
}
