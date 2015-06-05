package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class FileReaderTest extends AbstractJUnitSpringTest {
    @Autowired private FileReader fileReader;

    @Before
    public void setUp() {
    }

    @Test(expected = FileNotValidException.class)
    public void should_throw_exception_given_file_with_illegal_characters() throws IOException, FileNotValidException {
        fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.1");
    }

    @Test
    public void should_return_valid_lines_given_valid_file() throws IOException, FileNotValidException {
        LanguageFile languageFile = fileReader.readAllLinesWithCharacterCheck("./src/test/resources/dictionaryfiles/ENGLISH.2");
        List<String> lines = languageFile.getLines();
        assertThat(lines.contains("tomorrow, hmm...."), is(true));
        assertThat(lines.contains("I wonder what will happen"), is(true));

        assertThat(languageFile.getFileName(), is("ENGLISH.2"));
    }

    @Test
    public void should_return_list_of_valid_files_in_a_directory() throws IOException {
        List<LanguageFile> languageFiles = fileReader.readDirectory("./src/test/resources/dictionaryfiles");
        assertThat(languageFiles, notNullValue());
        assertThat(languageFiles.size(), is(4));
    }
}