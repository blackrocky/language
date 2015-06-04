package language;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        System.out.println("Hello world: ");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        System.out.println("bean Names: ");
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
        System.out.println("end of bean Names: ");
    }
}
