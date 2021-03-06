package language;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Dictionary {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    public static final String NON_ALPHABET_AND_SPACE_REGEX = "[^a-zA-Z ]";
    private Map<String, Set<String>> dictionary = new ConcurrentHashMap<>();

    public Dictionary() {
    }

    public void readAndStore(File dictionaryFile) {
        dictionaryFile.getLines().forEach(line -> {
            if (StringUtils.isNotBlank(line)) {
                storeLineInDictionary(line, dictionaryFile.getLanguage());
            }
        });
    }

    public void storeLineInDictionary(String line, String language) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalStateException();
        }
        line = StringUtils.lowerCase(line.replaceAll(Dictionary.NON_ALPHABET_AND_SPACE_REGEX, ""));

        String[] wordArray = StringUtils.split(line);
        if (wordArray == null) return;

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
