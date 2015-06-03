package language;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageFileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageFileReader.class);

    public static final String LEGAL_CHARACTERS_REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";

    public LanguageFile readAllLinesWithCharacterCheck(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        LOGGER.debug("Reading file {}", path.getFileName().toString());

        String language = StringUtils.substringBefore(path.getFileName().toString(), ".");
        Pattern pattern = Pattern.compile(LEGAL_CHARACTERS_REGEX);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            LOGGER.trace("line = {}", line);
            LOGGER.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line)) {
                if (!matcher.matches()) {
                    throw new IllegalStateException("File " + pathStr + " contains illegal characters");
                }
            }
        }
        return new LanguageFile(lines, language);
    }
}
