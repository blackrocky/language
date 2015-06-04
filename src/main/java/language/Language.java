package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

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

    public FileReader getFileReader() {
        return fileReader;
    }

    public void setFileReader(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String determineLanguage(String pathStr, String pathDictionaryStr) throws IOException, FileNotValidException {
        List<LanguageFile> dictionaryFiles = fileReader.readDirectory(pathDictionaryStr);
        for (LanguageFile file : dictionaryFiles) {
            try {
                dictionary.readAndStore(file.getParent() + "/" + file.getFileName()); // TODO find better way
            } catch (FileNotValidException e) {
                LOGGER.debug("File not valid for {} - {}", file.getFileName(), e.getMessage());
                continue;
            }
        }
        return determineLanguage(pathStr, dictionaryFiles);
    }

	public String determineLanguage(String pathStr, List<LanguageFile> dictionaryFiles) throws IOException, FileNotValidException {
		LanguageFile languageFile = fileReader.readAllLinesWithCharacterCheck(pathStr);

		Map<String, Integer> languageScore = new HashMap<>();
        for (LanguageFile dictionaryFile : dictionaryFiles) {
            languageScore.put(dictionaryFile.getLanguage(), 0);
        }

		for (String line : languageFile.getLines()) {
			LOGGER.debug("line = {}", line);

			Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();
			for (Map.Entry<String, Set<String>> entry : dictionaryMap.entrySet()) {
				LOGGER.debug("Checking {}", entry.getKey());
				Set<String> dictionaryWords = entry.getValue();

				line = StringUtils.lowerCase(line.replaceAll(Dictionary.NON_ALPHABET_AND_SPACE_REGEX, ""));
				String[] wordArray = StringUtils.split(line);
				Set<String> words = new HashSet<>(Arrays.asList(wordArray));

				if (words != null) {
					for (String word : words) {
						if (dictionaryWords.contains(word)) {
							languageScore.put(entry.getKey(), languageScore.get(entry.getKey()) + 1);
						}
					}
				}
			}
		}
		LOGGER.debug("languageScore: {}", languageScore);

		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> scoreEntry : languageScore.entrySet()) {
			if (maxEntry == null || scoreEntry.getValue() > maxEntry.getValue()) {
				maxEntry = scoreEntry;
			}
		}
        boolean allZero = true;
        for (Map.Entry<String, Integer> scoreEntry : languageScore.entrySet()) {
            if (scoreEntry.getValue() > 0) {
                allZero = false;
                break;
            }
        }

        if (maxEntry == null || allZero) {
            return UNKNOWN;
        }
		return maxEntry.getKey();
	}
}
