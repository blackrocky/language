package language;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DictionaryTest {
    private Dictionary dictionary;
    public static final String ENGLISH = "ENGLISH";
    public static final String INDONESIAN = "INDONESIAN";

    @Before
    public void setUp() {
        dictionary = new Dictionary(new LanguageFileReader());
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_null_word() {
        dictionary.storeLineInDictionary(null, ENGLISH);
    }

    @Test(expected = IllegalStateException.class)
    public void should_throw_exeption_given_blank_word() {
        dictionary.storeLineInDictionary("", ENGLISH);
    }

    @Test
    public void should_return_same_word_given_plain_word_input() {
        dictionary.storeLineInDictionary("Hello world!", ENGLISH);
        Map<String, Set<String>> dictionary = this.dictionary.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        assertThat(dictionary.get(ENGLISH), notNullValue());
        assertThat(dictionary.get(ENGLISH).size(), is(2));
        assertThat(dictionary.get(ENGLISH).contains("hello"), is(true));
        assertThat(dictionary.get(ENGLISH).contains("world") , is(true));
    }

    @Test
    public void should_read_and_store_one_file() throws IOException {
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.2");
        Map<String, Set<String>> dictionary = this.dictionary.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        Set<String> words = dictionary.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(7));
        assertThat(words.contains("i"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_one_language() throws IOException {
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.2");
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.3");
        Map<String, Set<String>> dictionary = this.dictionary.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(1));
        Set<String> words = dictionary.get(ENGLISH);
        assertThat(words, notNullValue());
        assertThat(words.size(), is(92));
        assertThat(words.contains("i"), is(true));
        assertThat(words.contains("wonder"), is(true));
        assertThat(words.contains("what"), is(true));
        assertThat(words.contains("will"), is(true));
        assertThat(words.contains("happen"), is(true));
        assertThat(words.contains("tomorrow"), is(true));
        assertThat(words.contains("hmm"), is(true));
        assertThat(words.contains("saint"), is(true));
        assertThat(words.contains("seiya"), is(true));
    }

    @Test
    public void should_read_and_store_multiple_files_multiple_languages() throws IOException {
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.2");
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.3");
        dictionary.readAndStore("./src/test/resources/languagefiles/INDONESIAN.1");
        Map<String, Set<String>> dictionary = this.dictionary.getDictionary();
        assertThat(dictionary, notNullValue());
        assertThat(dictionary.size(), is(2));

        Set<String> englishWords = dictionary.get(ENGLISH);
        assertThat(englishWords, notNullValue());
        assertThat(englishWords.size(), is(92));
        assertThat(englishWords.contains("i"), is(true));
        assertThat(englishWords.contains("wonder"), is(true));
        assertThat(englishWords.contains("what"), is(true));
        assertThat(englishWords.contains("will"), is(true));
        assertThat(englishWords.contains("happen"), is(true));
        assertThat(englishWords.contains("tomorrow"), is(true));
        assertThat(englishWords.contains("hmm"), is(true));
        assertThat(englishWords.contains("saint"), is(true));
        assertThat(englishWords.contains("seiya"), is(true));

        Set<String> indonesianWords = dictionary.get(INDONESIAN);
        assertThat(indonesianWords, notNullValue());
        assertThat(indonesianWords.size(), is(89));
        assertThat(indonesianWords.contains("bali"), is(true));
        assertThat(indonesianWords.contains("tanah"), is(true));
        assertThat(indonesianWords.contains("rot"), is(true));
        assertThat(indonesianWords.contains("lot"), is(true));
        assertThat(indonesianWords.contains("kembari"), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void should_return_exception_given_file_with_illegal_character() throws IOException {
        dictionary.readAndStore("./src/test/resources/languagefiles/ENGLISH.1");
    }

    // TODO
//    @Test
//    public void should_read_all_files_in_directory() {
//        dictionaryBuilder.readAndStore();
//    }
}
