package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void should_handle_exception_in_dictionary() throws IOException, FileNotValidException {
        doThrow(new FileNotValidException("file not valid")).when(dictionary).readAndStore(any(File.class));

        List<File> dictionaryFiles = new ArrayList<>();
        dictionaryFiles.add(new File(Arrays.asList(new String[]{"hello", "world"}), "mock parent1", "mock fileName1"));
        dictionaryFiles.add(new File(Arrays.asList(new String[]{"hello2", "world2"}), "mock parent2", "mock fileName2"));
        when(fileReader.readDirectory(anyString())).thenReturn(dictionaryFiles);

        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenReturn(new File(Arrays.asList(new String[]{"hello3", "world3"}), "mock parent3", "mock fileName3"));

        String languageStr = language.determineLanguage("mock path str", "mock dictionary str");
        assertThat(languageStr, is("UNKNOWN"));
    }

    @Test
    public void should_handle_not_valid_exception_in_file_reader() throws IOException, FileNotValidException {
        when(fileReader.readAllLinesWithCharacterCheck(anyString())).thenThrow(new FileNotValidException("mock file not valid exception"));
        String languageStr = language.determineLanguage("mock path str", "mock path dictionary str");
        assertThat(languageStr, is("UNKNOWN"));
    }

}
