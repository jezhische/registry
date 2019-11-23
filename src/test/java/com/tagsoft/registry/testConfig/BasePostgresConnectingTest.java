package com.tagsoft.registry.testConfig;

import com.tagsoft.registry.RegistryApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
 @SpringBootTest(classes = {RegistryApplication.class})
@Transactional
// to avoid automatic rollback if I need to add entry in db by test. If I don't need to commit an entry in db, I can
// to point out @Rollback directly over the test method.
@Rollback(value = false)
// https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-initialize-a-database-using-hibernate
// https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html
// To run tests against a real database:
@AutoConfigureTestDatabase(/*connection = EmbeddedDatabaseConnection.NONE,*/ replace = AutoConfigureTestDatabase.Replace.NONE)
public class BasePostgresConnectingTest {
}
