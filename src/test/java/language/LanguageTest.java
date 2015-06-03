package language;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LanguageTest {
    private Language language;
    @Before
    public void setUp() throws IOException {
        Dictionary db = new Dictionary(new LanguageFileReader());
        db.readAndStore("./src/test/resources/languagefiles/ENGLISH.2");
        db.readAndStore("./src/test/resources/languagefiles/ENGLISH.3");
        db.readAndStore("./src/test/resources/languagefiles/INDONESIAN.1");

        language = new Language(db);
    }

    @Test
    public void should_return_indonesian_given_text() throws IOException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT.txt");
        assertThat(languageStr, is("INDONESIAN"));
    }
}
