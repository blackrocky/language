package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class LanguageMockTest {
    @Mock private Language language;
    @Mock private FileReader fileReader;
    @Mock private Dictionary dictionary;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        language = new Language(fileReader, dictionary, "mock text file name", "mock text file folder", "mock dictionary folder");
    }

    @Test
    public void should_handle_not_valid_exception_in_file_reader() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenThrow(new FileNotValidException("mock file not valid exception"));
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_io_exception() throws IOException {
        when(fileReader.readDirectory(anyString())).thenThrow(new IOException());
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_null_input_file() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenReturn(null);
        String languageStr = language.determineLanguage();
        assertThat(languageStr, is("UNKNOWN"));
    }
}
