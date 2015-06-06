package language;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class FileTest {
    private File file;

    @Before
    public void setUp() {
        file = new File(Arrays.asList("hello", "world"), "directory", "english");
    }
    @Test
    public void should_return_correct_field_variables_for_file() {
        assertThat(file.getParent(), is("directory"));
        assertThat(file.getFileName(), is("english"));
        assertThat(file.getLanguage(), is("ENGLISH"));
        assertThat(file.getLines(), notNullValue());
        assertThat(file.getLines().get(0), is("hello"));
        assertThat(file.getLines().get(1), is("world"));
    }

    @Test
    public void should_be_equal_file() {
        File file2 = new File(Arrays.asList("hello", "world"), "directory", "english");
        assertThat(file, is(file2));
    }

}