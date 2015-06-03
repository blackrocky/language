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

public class Language {
	private static final Logger LOGGER = LoggerFactory.getLogger(Language.class);

	public String determineLanguage(String pathStr) throws IOException {
		Path path = Paths.get(pathStr);
		List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
		LOGGER.debug("Reading file {}", path.getFileName().toString());
		for (String line : lines) {
			LOGGER.debug("line = {}", line);
		}

		return "";
	}

	public static void main(String[] args) {
		LocalTime localTime = new LocalTime();
		System.out.println("Hello world: " + localTime);
	}
}
