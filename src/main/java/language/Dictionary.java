package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Dictionary {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    public static final String NON_ALPHABET_AND_SPACE_REGEX = "[^a-zA-Z ]";

    private FileReader fileReader;
    private Map<String, Set<String>> dictionary = new ConcurrentHashMap<>();

    @Autowired
    public Dictionary(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public void readAndStore(LanguageFile languageFile) throws IOException, FileNotValidException {
        for (String line : languageFile.getLines()) {

            if (StringUtils.isNotBlank(line)) {
                storeLineInDictionary(line, languageFile.getLanguage());
            }
        }
    }

    public void storeLineInDictionary(String line, String language) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalStateException();
        }
        line = StringUtils.lowerCase(line.replaceAll(Dictionary.NON_ALPHABET_AND_SPACE_REGEX, ""));

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
