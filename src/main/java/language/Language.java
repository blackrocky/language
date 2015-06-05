package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static language.Dictionary.*;

@Component
public class Language {
    private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);

    private static final String UNKNOWN = "UNKNOWN";
    private FileReader fileReader;
    private Dictionary dictionary;

    public Language() {
    }

    @Autowired
    public Language(FileReader fileReader, Dictionary dictionary) {
        this.fileReader = fileReader;
        this.dictionary = dictionary;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public String determineLanguage(String pathStr, String pathDictionaryStr) {
        try {
            List<File> dictionaryFiles = fileReader.readDirectory(pathDictionaryStr);
            for (File dictionaryFile : dictionaryFiles) {
                try {
                    dictionary.readAndStore(dictionaryFile);
                } catch (FileNotValidException e) {
                    LOGGER.error("File not valid for {} - {}", dictionaryFile.getFileName(), e.getMessage());
                }
            }
            return determineLanguage(pathStr, dictionaryFiles);
        } catch (IOException ex) {
            return UNKNOWN;
        }
    }

    private String determineLanguage(String pathStr, List<File> dictionaryFiles) throws IOException {
        File inputFile;
        try {
            inputFile = fileReader.readAllLinesWithCharacterCheck(pathStr);
        } catch (FileNotValidException e) {
            return UNKNOWN;
        }

        Map<String, Integer> languageScore = calculateScore(dictionaryFiles, inputFile);

        Map.Entry<String, Integer> maxEntry = null;
        int totalScore = 0;
        for (Map.Entry<String, Integer> scoreEntry : languageScore.entrySet()) {
            if (maxEntry == null || scoreEntry.getValue() > maxEntry.getValue()) {
                maxEntry = scoreEntry;
            }
            totalScore += scoreEntry.getValue();
        }

        if (maxEntry == null || totalScore == 0) {
            return UNKNOWN;
        }
        return maxEntry.getKey();
    }

    private Map<String, Integer> calculateScore(List<File> dictionaryFiles, File inputFile) {
        Map<String, Integer> languageScore = new HashMap<>();
        for (File dictionaryFile : dictionaryFiles) {
            languageScore.put(dictionaryFile.getLanguage(), 0);
        }

        for (String line : inputFile.getLines()) {
            LOGGER.debug("line = {}", line);

            Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();
            for (Map.Entry<String, Set<String>> entry : dictionaryMap.entrySet()) {
                LOGGER.debug("Checking {}", entry.getKey());
                Set<String> dictionaryWords = entry.getValue();

                line = StringUtils.lowerCase(line.replaceAll(NON_ALPHABET_AND_SPACE_REGEX, ""));
                String[] wordArray = StringUtils.split(line);
                Set<String> words = new HashSet<>(Arrays.asList(wordArray));

                for (String word : words) {
                    if (dictionaryWords.contains(word)) {
                        languageScore.put(entry.getKey(), languageScore.get(entry.getKey()) + 1);
                    }
                }
            }
        }
        LOGGER.debug("languageScore: {}", languageScore);
        return languageScore;
    }
}
