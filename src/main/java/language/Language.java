package language;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Language {
	private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);
	private Dictionary dictionary;

	public Language(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String determineLanguage(String pathStr) throws IOException {
		LanguageFileReader languageFileReader = new LanguageFileReader();
		LanguageFile languageFile = languageFileReader.readAllLinesWithCharacterCheck(pathStr);

		Map<String, Integer> languageScore = new HashMap<>();
		// TODO initialise available languages automatically
		languageScore.put("ENGLISH", 0);
		languageScore.put("INDONESIAN", 0);

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

		int max = 0;
		for (Map.Entry<String, Integer> scoreEntry : languageScore.entrySet()) {
			if (scoreEntry.getValue() > max) {
				max = scoreEntry.getValue();
			}
		}
		return "";
	}

	public static void main(String[] args) throws IOException {
		LocalTime localTime = new LocalTime();
		System.out.println("Hello world: " + localTime);

	}

}
