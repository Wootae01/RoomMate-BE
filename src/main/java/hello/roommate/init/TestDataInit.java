package hello.roommate.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class TestDataInit {
    private final OptionInit optionInit;
    private final MemberInit memberInit;
    private final LifeStyleInit lifeStyleInit;
    private final PreferenceInit preferenceInit;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        optionInit.createOption();
        memberInit.createMember();
        lifeStyleInit.createLifeStyle();
        preferenceInit.createPreference();
    }

}
