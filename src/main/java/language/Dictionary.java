package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Dictionary {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    public static final String LEGAL_CHARACTERS_REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";
    public static final String NON_ALPHABET_AND_SPACE_REGEX = "[^a-zA-Z ]";

    private FileReader fileReader;
    private Map<String, Set<String>> dictionary = new HashMap<>();

    @Autowired
    public Dictionary(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public void readAndStore(LanguageFile languageFile) throws IOException, FileNotValidException {
        Pattern pattern = Pattern.compile(LEGAL_CHARACTERS_REGEX);
        for (String line : languageFile.getLines()) {
            Matcher matcher = pattern.matcher(line);
            LOGGER.trace("line = {}", line);
            LOGGER.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line)) {
                if (!matcher.matches()) {
                    throw new FileNotValidException("File " + languageFile.getFileName() + " contains illegal characters");
                }
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
