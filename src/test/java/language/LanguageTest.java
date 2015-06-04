package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LanguageTest {
    private Language language;

    @Before
    public void setUp() throws IOException, FileNotValidException {
        LanguageFileReader reader = new LanguageFileReader();
        List<LanguageFile> languageFileList = reader.readDirectory("./src/test/resources/languagefiles");

        Dictionary dictionary = new Dictionary(reader);
        for (LanguageFile file : languageFileList) {
            dictionary.readAndStore(file.getParent() + "/" + file.getFileName()); // TODO find better way
        }

        language = new Language(dictionary);
    }

    @Test
    public void should_return_indonesian_given_text() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT.txt");
        assertThat(languageStr, is("INDONESIAN"));
    }
}
