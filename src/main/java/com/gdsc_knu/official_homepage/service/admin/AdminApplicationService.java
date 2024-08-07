package com.gdsc_knu.official_homepage.service.admin;

import com.gdsc_knu.official_homepage.dto.PagingResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.AdminApplicationResponse;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {
    private final ApplicationRepository applicationRepository;

    @Transactional(readOnly = true)
    public AdminApplicationResponse.Statistics getStatistic() {
        ApplicationStatisticType statistic = applicationRepository.getStatistics();
        return AdminApplicationResponse.Statistics.of(
                statistic.getTotal(),
                statistic.getOpenCount(),
                statistic.getTotal() - statistic.getOpenCount(),
                statistic.getApprovedCount(),
                statistic.getTotal() - statistic.getApprovedCount());
    }


    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByOption(int page, int size, Track track, boolean isMarked){
        Page<Application> applicationPage
                = applicationRepository.findAllApplicationsByOption(PageRequest.of(page,size), track, isMarked);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }

    @Transactional
    public void markApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        application.changeMark();
    }


    @Transactional
    public void decideApplication(Long id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        if (status.equals(ApplicationStatus.APPROVED.name()))
            application.approve();
        else if (status.equals(ApplicationStatus.REJECTED.name()))
            application.reject();
    }


    @Transactional(readOnly = true)
    public AdminApplicationResponse.Detail getApplicationDetail(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        if (!application.isOpened())
            application.open();
        return AdminApplicationResponse.Detail.from(application);
    }

    @Transactional
    public void noteApplication(Long id, String note) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 지원서류가 없습니다."));
        application.saveNote(note);
    }


    @Transactional(readOnly = true)
    public PagingResponse<AdminApplicationResponse.Overview> getApplicationsByName(int page, int size, String name){
        Page<Application> applicationPage
                = applicationRepository.findByNameContaining(PageRequest.of(page,size), name);
        return PagingResponse.from(applicationPage, AdminApplicationResponse.Overview::from);
    }
}
