package com.bobocode.config;

import com.bobocode.TestDataGenerator;
import com.bobocode.dao.AccountDao;
import com.bobocode.dao.FakeAccountDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class specifies application context configuration. It tells Spring to scan "dao" and "service" packages in order
 * to find and create instances for {@link com.bobocode.dao.FakeAccountDao} and
 * {@link com.bobocode.service.AccountService}.
 * <p>
 * It also explicitly configures a bean of {@link TestDataGenerator} called "dataGenerator". This beans will be injected
 * into {@link com.bobocode.dao.FakeAccountDao} in order to generate some fake accounts.
 */

@ComponentScan({"com.bobocode.dao","com.bobocode.service"})
@Configuration
public class ApplicationConfig {
    // todo: configure application context according to javadoc by following tests in ApplicationConfigTest
    // todo: verify final implementation by running ApplicationContextTest
    @Bean
    public TestDataGenerator dataGenerator(){
        return new TestDataGenerator();
    }


}
