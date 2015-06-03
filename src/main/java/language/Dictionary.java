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

public class Dictionary {
    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    public static final String LEGAL_CHARACTERS_REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";
    public static final String NON_ALPHABET_AND_SPACE_REGEX = "[^a-zA-Z ]";

    private LanguageFileReader languageFileReader;
    private Map<String, Set<String>> dictionary = new HashMap<>();

    public Dictionary(LanguageFileReader languageFileReader) {
        this.languageFileReader = languageFileReader;
    }

    public void readAndStore(String pathStr) throws IOException {
        LanguageFile languageFile = languageFileReader.readAllLinesWithCharacterCheck(pathStr);
//        Path path = Paths.get(pathStr);
//        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
//        LOGGER.debug("Reading file {}", path.getLanguage().toString());
//
//\\        String language = StringUtils.substringBefore(path.getLanguage().toString(), ".");
        Pattern pattern = Pattern.compile(LEGAL_CHARACTERS_REGEX);
        for (String line : languageFile.getLines()) {
            Matcher matcher = pattern.matcher(line);
            LOGGER.trace("line = {}", line);
            LOGGER.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line)) {
                if (!matcher.matches()) {
                    throw new IllegalStateException("File " + pathStr + " contains illegal characters");
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
