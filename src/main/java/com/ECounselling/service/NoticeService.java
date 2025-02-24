package com.ECounselling.service;

import com.ECounselling.model.Notice;
import com.ECounselling.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    public Notice addNotice(Notice notice) {
        notice.setPublishedDate(LocalDateTime.now());
        return noticeRepository.save(notice);
    }
}
