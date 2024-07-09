package com.gdsc_knu.official_homepage.entity.application;

import com.gdsc_knu.official_homepage.dto.application.ApplicationAnswerDTO;
import com.gdsc_knu.official_homepage.dto.application.ApplicationRequest;
import com.gdsc_knu.official_homepage.entity.BaseTimeEntity;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Application extends BaseTimeEntity {
    @Id
    @Column(name = "application_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String studentNumber;

    private String major;

    private String email;

    private String phoneNumber;

    private String techStack;

    private String links;

    private ApplicationStatus applicationStatus;

    private Track track;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "application_id")
    private List<ApplicationAnswer> answers = new ArrayList<>();

    public void updateApplication(ApplicationRequest applicationRequest) {
        this.name = applicationRequest.getName();
        this.studentNumber = applicationRequest.getStudentNumber();
        this.major = applicationRequest.getMajor();
        this.email = applicationRequest.getEmail();
        this.phoneNumber = applicationRequest.getPhoneNumber();
        this.techStack = applicationRequest.getTechStack();
        this.links = applicationRequest.getLinks();
        this.applicationStatus = applicationRequest.getApplicationStatus();
        this.track = applicationRequest.getTrack();
        updateNewAnswers(applicationRequest.getAnswers());
    }

    private void updateNewAnswers(List<ApplicationAnswerDTO> newAnswersDTO) {
        Map<Integer, String> newAnswers = newAnswersDTO.stream()
                .collect(Collectors.toMap(ApplicationAnswerDTO::getQuestionNumber, ApplicationAnswerDTO::getAnswer));
        Map<Integer, ApplicationAnswer> oldAnswers = this.answers.stream()
                .collect(Collectors.toMap(ApplicationAnswer::getQuestionNumber, answer -> answer));
        int answerSize = oldAnswers.size();
        if (answerSize != newAnswers.size()) {
            throw new IllegalArgumentException("지원서의 질문 응답 개수가 일치하지 않습니다.");
        }
        for (int i = 0; i < answerSize; i++) {
            if (!oldAnswers.get(i).getAnswer().equals(newAnswers.get(i))) {
                oldAnswers.get(i).updateAnswer(newAnswers.get(i));
            }
        }
    }
}