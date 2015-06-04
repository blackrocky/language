package language;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Language.class, Dictionary.class, FileReader.class})
public abstract class AbstractJUnitTest {
}