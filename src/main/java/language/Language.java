package language;

import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Language {
	private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);
	private Dictionary dictionary;

	public Language(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String determineLanguage(String pathStr) throws IOException {
		LanguageFileReader languageFileReader = new LanguageFileReader();
		LanguageFile languageFile = languageFileReader.readAllLinesWithCharacterCheck(pathStr);

		for (String line : languageFile.getLines()) {
			LOGGER.debug("line = {}", line);

			Map<String, Set<String>> dictionaryMap = dictionary.getDictionary();

		}

		return "";
	}

	public static void main(String[] args) throws IOException {
		LocalTime localTime = new LocalTime();
		System.out.println("Hello world: " + localTime);

	}

}
