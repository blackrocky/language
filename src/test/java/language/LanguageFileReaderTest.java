package language;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LanguageFileReaderTest {
    private LanguageFileReader languageFileReader;

    @Before
    public void setUp() {
        languageFileReader = new LanguageFileReader();
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_file_with_illegal_characters() throws IOException {
        languageFileReader.readAllLinesWithCharacterCheck("./src/test/resources/languagefiles/ENGLISH.1");
    }

    @Test
    public void should_return_valid_lines_given_valid_file() throws IOException {
        LanguageFile languageFile = languageFileReader.readAllLinesWithCharacterCheck("./src/test/resources/languagefiles/ENGLISH.2");
        List<String> lines = languageFile.getLines();
        assertThat(lines.contains("tomorrow, hmm...."), is(true));
        assertThat(lines.contains("I wonder what will happen"), is(true));

        assertThat(languageFile.getFileName(), is("ENGLISH"));
    }
}