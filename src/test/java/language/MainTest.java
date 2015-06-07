package language;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MainTest {
    private Language language;

    @Before
    public void setUp() {
        language = new Language();
    }

    @Test
    public void testUserInput() throws Exception {
        try (ByteArrayInputStream in = new ByteArrayInputStream("My text file name\nMy text file folder\nMy dictionary folder".getBytes())) {
            Main.userInput(language, "default1", "default2", "default3", in);

            assertThat(language.getTextFileName(), is("My text file name"));
            assertThat(language.getTextFileFolder(), is("My text file folder"));
            assertThat(language.getDictionaryFolder(), is("My dictionary folder"));
        }
    }
}