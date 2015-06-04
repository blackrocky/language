package language;

import language.exception.FileNotValidException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    public static final String LEGAL_CHARACTERS_REGEX = "^[a-zA-Z \\.\\,\\;\\:]+$";

    public FileReader() {
    }

    public List<LanguageFile> readDirectory(String directory) throws IOException {
        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        } catch (IOException ex) {
            throw ex;
        }

        LOGGER.trace("fileNames: {}", fileNames);
        List<LanguageFile> languageFiles = new ArrayList<>();
        for (String fileName : fileNames) {
            try {
                LanguageFile languageFile = readAllLinesWithCharacterCheck(fileName);
                languageFiles.add(languageFile);
            } catch (FileNotValidException e) {
                continue;
            }
        }

        return languageFiles;
    }

    public LanguageFile readAllLinesWithCharacterCheck(String pathStr) throws IOException, FileNotValidException {
        Path path = Paths.get(pathStr);
        List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
        LOGGER.debug("Reading file {}", path.getFileName().toString());

        String parent = path.getParent().toString();
        String fileName = path.getFileName().toString();
        Pattern pattern = Pattern.compile(LEGAL_CHARACTERS_REGEX);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            LOGGER.trace("line = {}", line);
            LOGGER.trace("matches {}", matcher.matches());

            if (StringUtils.isNotBlank(line)) {
                if (!matcher.matches()) {
                    LOGGER.error("File {} has illegal character(s)", path.getFileName().toString());
                    throw new FileNotValidException("File " + pathStr + " contains illegal characters");
                }
            }
        }
        return new LanguageFile(lines, parent, fileName);
    }
}
