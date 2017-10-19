package examples.cordapps.two

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

// TODO reading properties inside dependencies requires Spring-specific annotations (@PropertySource, @Configuration) and a Spring-compatible DI container.
@Configuration
@PropertySource("classpath:cordapp2.properties")
open class AppConfig