package language;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class LanguageReaderTest {

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_null_word() {
        String word = LanguageReader.getWord(null);
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_blank_word() {
        String word = LanguageReader.getWord("");
    }

    @Test
    public void should_return_same_word_given_plain_word_input() {
        String word = LanguageReader.getWord("hello");
        assertThat(word, is("hello"));
    }
}
