package com.ECounselling.service;

import com.ECounselling.model.CounsellingStatus;
import com.ECounselling.model.Status;
import com.ECounselling.repository.CounsellingStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounsellingStatusService {

    @Autowired
    private CounsellingStatusRepository counsellingStatusRepository;

    public CounsellingStatus getCounsellingStatus() {
        return counsellingStatusRepository.findById(1L)
                .map(status -> {
                    if (status.getStatus() == null) {
                        status.setStatus(Status.NOT_STARTED);
                        counsellingStatusRepository.save(status);
                    }
                    return status;
                })
                .orElseGet(() -> {
                    CounsellingStatus newStatus = new CounsellingStatus();
                    counsellingStatusRepository.save(newStatus);
                    return newStatus;
                });
    }

    public CounsellingStatus updateStatus(Status newStatus) {
        CounsellingStatus counsellingStatus = counsellingStatusRepository.findById(1L)
                .orElse(new CounsellingStatus());
        counsellingStatus.setStatus(newStatus);
        return counsellingStatusRepository.save(counsellingStatus);
    }

}
