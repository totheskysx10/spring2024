package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.ComplaintService;
import com.spring.vsurin.bookexchange.domain.ComplaintSubject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/complaint")
    public ResponseEntity<Void> sendComplaint(@RequestParam ComplaintSubject subject, @RequestParam long subjectId, @RequestParam String complaint) {
        if (subject == null || complaint.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        complaintService.sendComplaint(subject, subjectId, complaint);
        return ResponseEntity.ok().build();
    }
}
