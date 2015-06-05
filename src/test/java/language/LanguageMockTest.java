package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class LanguageMockTest {
    @Mock private Language language;
    @Mock private FileReader fileReader;
    @Mock private Dictionary dictionary;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        language = new Language(fileReader, dictionary);
    }

    @Test
    public void should_handle_exception() throws IOException, FileNotValidException {
        //when(fileReader.readDirectory(anyString())).thenThrow(new IOException("io error"));
        //doThrow(new FileNotValidException("file not valid")).when(dictionary).readAndStore(any(LanguageFile.class)));
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenThrow(new FileNotValidException("mock file not valid exception"));
        String languageStr = language.determineLanguage("mock path str", "mock path dictionary str");
        assertThat(languageStr, is("UNKNOWN"));
    }

}
