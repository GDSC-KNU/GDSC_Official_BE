package com.gdsc_knu.official_homepage.dto.admin.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AdminApplicationResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {
        private Integer total;
        private Integer openCount;
        private Integer notOpenCount;
        private Integer approvedCount;
        private Integer rejectedCount;

        public static Statistics of(int total, int openCount, int notOpenCount, int approvedCount, int rejectedCount) {
            return Statistics.builder()
                    .total(total)
                    .openCount(openCount)
                    .notOpenCount(notOpenCount)
                    .approvedCount(approvedCount)
                    .rejectedCount(rejectedCount)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Overview {
        private Long id;
        private String name;
        private LocalDateTime submittedAt;
        private String studentNumber;
        private String major;
        private String track;
        private boolean isOpen;
        private boolean isMarked;

        public static Overview from(Application application){
            return Overview.builder()
                    .id(application.getId())
                    .name(application.getName())
                    .submittedAt(application.getModifiedAt())
                    .studentNumber(application.getStudentNumber())
                    .major(application.getMajor())
                    .track(application.getTrack().name())
                    .isOpen(application.isOpened())
                    .isMarked(application.isMarked())
                    .build();
        }
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id; // application id
        private String name;
        private String studentNumber;
        private String major;
        private String phoneNumber;
        private String email;
        private String track;
        private LocalDateTime submittedAt;
        private String techStack;
        private String link;
        private boolean isMarked;
        private String note;
        private List<Answer> answers;

        public static Detail from(Application application){
            return Detail.builder()
                    .id(application.getId())
                    .name(application.getName())
                    .studentNumber(application.getStudentNumber())
                    .major(application.getMajor())
                    .phoneNumber(application.getPhoneNumber())
                    .email(application.getEmail())
                    .track(application.getTrack().name())
                    .submittedAt(application.getModifiedAt())
                    .techStack(application.getTechStack())
                    .link(application.getLinks())
                    .isMarked(application.isMarked())
                    .note(application.getNote())
                    .answers(application.getAnswers().stream()
                            .map(Answer::from)
                            .toList())
                    .build();
        }

        @Getter
        @Builder
        @AllArgsConstructor
        private static class Answer {
            private int questionNumber;
            private String answer;
            public static Answer from(ApplicationAnswer applicationAnswer){
                return Answer.builder()
                        .questionNumber(applicationAnswer.getQuestionNumber())
                        .answer(applicationAnswer.getAnswer())
                        .build();
            }

        }
    }
}
