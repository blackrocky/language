package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LanguageTest extends AbstractJUnitSpringTest {
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
    public void should_return_unknown_given_text_with_all_zero_occurence() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT2.txt", "./src/test/resources/dictionaryfiles2");
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_return_either_given_same_words() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT3.txt", "./src/test/resources/dictionaryfiles3");
        assertThat(languageStr, is("ENGLISH"));
    }

    @Test
    public void should_return_spanish() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT4.txt", "./src/test/resources/dictionaryfiles4");
        assertThat(languageStr, is("SPANISH"));
    }
}
