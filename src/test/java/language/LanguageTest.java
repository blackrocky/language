package language;

import language.exception.FileNotValidException;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LanguageTest extends AbstractJUnitTest {
    @Autowired private Language language;

    @Before
    public void setUp() throws IOException, FileNotValidException {
        language.getDictionary().getDictionary().clear();
    }

    @Test
    public void should_return_indonesian_given_text() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT.txt", "./src/test/resources/dictionaryfiles");
        assertThat(languageStr, is("INDONESIAN"));
    }

    @Test
    public void should_return_unknown_given_text() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT2.txt", "./src/test/resources/dictionaryfiles2");
        assertThat(languageStr, is("UNKNOWN"));
    }
}
