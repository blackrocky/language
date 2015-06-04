package language;

import language.exception.FileNotValidException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-test.xml"})
public class LanguageTest {
    @Autowired private LanguageFileReader languageFileReader;
    @Autowired private Dictionary dictionary;
    @Autowired private Language language;
    private List<LanguageFile> dictionaryFiles;

    @Before
    public void setUp() throws IOException, FileNotValidException {
        dictionary.getDictionary().clear();
        dictionaryFiles = languageFileReader.readDirectory("./src/test/resources/languagefiles");
        for (LanguageFile file : dictionaryFiles) {
            dictionary.readAndStore(file.getParent() + "/" + file.getFileName()); // TODO find better way
        }
    }

    @Test
    public void should_return_indonesian_given_text() throws IOException, FileNotValidException {
        String languageStr = language.determineLanguage("./src/test/resources/textfile/TEXT.txt", dictionaryFiles);
        assertThat(languageStr, is("INDONESIAN"));
    }
}
