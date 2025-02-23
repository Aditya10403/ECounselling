package com.ECounselling.service;

import com.ECounselling.model.AllocationResult;
import com.ECounselling.repository.AllocationResultRepository;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllocationResultService {

    @Autowired
    private AllocationResultRepository allocationResultRepository;

    public ApiResponse getResultsByStudentName(String studentName) {
        Optional<AllocationResult> allocationResult = allocationResultRepository.findByStudentName(studentName);

        if (allocationResult.isEmpty() || "Not Allocated".equals(allocationResult.get().getDepartmentName())) {
            return new ApiResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Student not allocated to any college",
                    null
            );
        }
        return new ApiResponse(HttpStatus.OK.value(),
                "Student allocation retrieved",
                allocationResult.get()
        );
    }

    public ApiResponse getResultsByCollegeName(String collegeName) {
        List<AllocationResult> results = allocationResultRepository.findByCollegeName(collegeName);
        if (results != null && !results.isEmpty()) {
            return new ApiResponse(
                    HttpStatus.OK.value(),
                    "Allocated students retrieved successfully",
                    results
            );
        }
        return new ApiResponse(
                HttpStatus.NOT_FOUND.value(),
                "No students allocated to this college",
                null
        );
    }
}