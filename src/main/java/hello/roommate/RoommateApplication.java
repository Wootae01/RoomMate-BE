package hello.roommate;

import hello.roommate.member.service.MemberService;
import hello.roommate.recommendation.service.LifestyleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RoommateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoommateApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(MemberService memberService, LifestyleService lifestyleService) {
		return new TestDataInit(lifestyleService, memberService);
	}
}
