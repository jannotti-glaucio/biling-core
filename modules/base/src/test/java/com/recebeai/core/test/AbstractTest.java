package tech.jannotti.billing.core.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreTestApplication.class)
@TestPropertySource("classpath:/application-test.yml")
public abstract class AbstractTest {

}
