package hello.roommate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import hello.roommate.init.LifeStyleInit;
import hello.roommate.init.MemberInit;
import hello.roommate.init.OptionInit;
import hello.roommate.init.TestDataInit;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class RoommateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoommateApplication.class, args);
	}

	@Bean
	@Profile({"local", "test"})
	public TestDataInit testDataInit(OptionInit optionInit, MemberInit memberInit, LifeStyleInit lifeStyleInit) {
		return new TestDataInit(optionInit, memberInit, lifeStyleInit);
	}
}
