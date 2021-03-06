package de.syslord.slidegen.editor.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.spring.annotation.EnableVaadin;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class, DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = "de.syslord.slidegen.editor")
@EnableAsync
@EnableScheduling
@Configuration
@EnableVaadin
public class SlideEditorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlideEditorApplication.class, args);
	}
}
