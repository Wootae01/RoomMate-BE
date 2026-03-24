package hello.roommate.member.domain;

import hello.roommate.chat.domain.Message;
import hello.roommate.chat.domain.Notification;
import hello.roommate.recommendation.domain.LifeStyle;
import hello.roommate.recommendation.domain.Preference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "nickname"),
                @UniqueConstraint(columnNames = "username")
        })
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(length = 10)
    private String nickname;
    private String introduce;
    private int age;

    @Enumerated(EnumType.STRING)
    private Dormitory dorm;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LifeStyle> lifeStyle = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Preference> preference = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Notification notification;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Message> messages;

    public Member(String nickname, String introduce, int age, Dormitory dorm, Gender gender) {
        this.nickname = nickname;
        this.introduce = introduce;
        this.age = age;
        this.dorm = dorm;
        this.gender = gender;
    }
}
