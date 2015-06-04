package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class Language {
	private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);

    private static final String UNKNOWN = "UNKNOWN";
	private Dictionary dictionary;

    public Language() {

    }

	public Language(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String determineLanguage(String pathStr) throws IOException, FileNotValidException {
		LanguageFileReader languageFileReader = new LanguageFileReader();
		LanguageFile languageFile = languageFileReader.readAllLinesWithCharacterCheck(pathStr);

		Map<String, Integer> languageScore = new HashMap<>();
        List<LanguageFile> dictionaryFiles = languageFileReader.readDirectory("./src/test/resources/languagefiles");
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
        if (maxEntry == null) {
            return UNKNOWN;
        }
		return maxEntry.getKey();
	}

	public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Language.class, args);
        System.out.println("Hello world: ");
	}

}
