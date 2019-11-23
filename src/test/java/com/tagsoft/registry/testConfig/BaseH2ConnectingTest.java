package com.tagsoft.registry.testConfig;

import com.tagsoft.registry.RegistryApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RegistryApplication.class, H2TestProfileJpaConfig.class})
// to avoid "failed to lazily initialize a collection of role: com.tagsoft.registry.model.Customer.roles, could not initialize proxy - no Session"
@Transactional
@ActiveProfiles("testH2") // see bean config in H2TestProfileJpaConfig
public class BaseH2ConnectingTest {
}
