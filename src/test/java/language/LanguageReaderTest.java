package language;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LanguageReaderTest {
    private LanguageReader languageReader;
    public static final String ENGLISH = "ENGLISH";
    public static final String INDONESIAN = "INDONESIAN";

    @Before
    public void setUp() {
        languageReader = new LanguageReader();
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_null_word() {
        languageReader.lineToDictionary(null, ENGLISH);
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_blank_word() {
        languageReader.lineToDictionary("", ENGLISH);
    }

    @Test
    public void should_return_same_word_given_plain_word_input() {
        languageReader.lineToDictionary("Hello world!", ENGLISH);
        Map<String, Set<String>> dictionary = languageReader.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        assertThat(dictionary.get(ENGLISH), notNullValue());
        assertThat(dictionary.get(ENGLISH).size(), is(2));
        assertThat(dictionary.get(ENGLISH).contains("Hello"), is(true));
        assertThat(dictionary.get(ENGLISH).contains("world") , is(true));
    }

    @Test
    public void should_read_and_store_one_file() throws IOException {
        languageReader.readAndStore("src/test/resources/languagefiles/ENGLISH.2");
        Map<String, Set<String>> dictionary = languageReader.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        Set<String> words = dictionary.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(7));
        assertThat(words.contains("I"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_one_language() throws IOException {
        languageReader.readAndStore("src/test/resources/languagefiles/ENGLISH.1");
        languageReader.readAndStore("src/test/resources/languagefiles/ENGLISH.2");
        Map<String, Set<String>> dictionary = languageReader.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        Set<String> words = dictionary.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(88));
        assertThat(words.contains("I"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
        assertThat(words.contains("Saint"), is(true));
        assertThat(words.contains("Seiya"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_multiple_languages() throws IOException {
        languageReader.readAndStore("src/test/resources/languagefiles/ENGLISH.1");
        languageReader.readAndStore("src/test/resources/languagefiles/ENGLISH.2");
        languageReader.readAndStore("src/test/resources/languagefiles/INDONESIAN.1");
        Map<String, Set<String>> dictionary = languageReader.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(2));

        Set<String> englishWords = dictionary.get(ENGLISH);
        assertThat(englishWords, notNullValue());
        assertThat(englishWords.size(), is(88));
        assertThat(englishWords.contains("I"), is(true));
        assertThat(englishWords.contains("wonder"), is(true));
        assertThat(englishWords.contains("what"), is(true));
        assertThat(englishWords.contains("will"), is(true));
        assertThat(englishWords.contains("happen"), is(true));
        assertThat(englishWords.contains("tomorrow"), is(true));
        assertThat(englishWords.contains("hmm"), is(true));
        assertThat(englishWords.contains("Saint"), is(true));
        assertThat(englishWords.contains("Seiya"), is(true));

        Set<String> indonesianWords = dictionary.get(INDONESIAN);
        assertThat(indonesianWords, notNullValue());
        assertThat(indonesianWords.size(), is(103));
        assertThat(indonesianWords.contains("Sumber"), is(true));
        assertThat(indonesianWords.contains("Bali"), is(true));
        assertThat(indonesianWords.contains("Tanah"), is(true));
        assertThat(indonesianWords.contains("Rot"), is(true));
        assertThat(indonesianWords.contains("Lot"), is(true));
        assertThat(indonesianWords.contains("KEMBARI"), is(true));
    }


    @Test
    public void should_remove_illegal_characters() {


    }
}
