package hello.roommate;

import hello.roommate.member.repository.MemberRepository;
import hello.roommate.recommendation.repository.LifeStyleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RoommateApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoommateApplication.class, args);
	}

}
