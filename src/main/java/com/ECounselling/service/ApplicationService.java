package com.ECounselling.service;

import com.ECounselling.model.AllocationResult;
import com.ECounselling.model.Application;
import com.ECounselling.model.Department;
import com.ECounselling.repository.AllocationResultRepository;
import com.ECounselling.repository.ApplicationRepository;
import com.ECounselling.repository.DepartmentRepository;
import com.ECounselling.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AllocationResultRepository allocationResultRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<AllocationResult> allocateDepartments() {
        List<Application> applications = applicationRepository.findAll();
        applications.sort(Comparator.comparing(Application::getErank));

        List<AllocationResult> results = new ArrayList<>();
        Map<String, Integer> seatAvailability = loadSeatAvailability();

        for (Application app : applications) {
            boolean allocated = false;

            List<Map<String, String>> preferences = Arrays.asList(
                    app.getFirstPreference(),
                    app.getSecondPreference(),
                    app.getThirdPreference()
            );

            for (Map<String, String> preference : preferences) {
                String key = getPreferenceKey(preference);

                if (isSeatAvailable(key, seatAvailability)) {
                    allocateStudent(results, seatAvailability, app, preference);
                    allocated = true;
                    break;
                }
            }

            if (!allocated) {
                results.add(new AllocationResult(app.getStudentName(), "Not Allocated", ""));
            }
        }

        allocationResultRepository.saveAll(results);
        return results;
    }

    private Map<String, Integer> loadSeatAvailability() {
        List<Department> departments = departmentRepository.findAll();
        Map<String, Integer> seatAvailability = new HashMap<>();

        for (Department dept : departments) {
            String key = dept.getDepartmentName() + " - " + dept.getCollege().getCollegeName();
            seatAvailability.put(key, dept.getNoOfSeats());
        }
        return seatAvailability;
    }

    private boolean isSeatAvailable(String key, Map<String, Integer> seatAvailability) {
        return seatAvailability.getOrDefault(key, 0) > 0;
    }

    private void allocateStudent(List<AllocationResult> results, Map<String, Integer> seatAvailability,
                                 Application app, Map<String, String> preference) {
        String key = getPreferenceKey(preference);
        seatAvailability.put(key, seatAvailability.get(key) - 1);

        results.add(new AllocationResult(
                app.getStudentName(),
                preference.get("departmentName"),
                preference.get("collegeName")
        ));
    }

    private String getPreferenceKey(Map<String, String> preference) {
        return preference.get("departmentName") + " - " + preference.get("collegeName");
    }

    public Boolean resetAllotmentProcess() {
        try {
            applicationRepository.deleteAll();
            allocationResultRepository.deleteAll();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public ApiResponse getApplicationByStudentName(String studentName) {
        Optional<Application> application = applicationRepository.findByStudentName(studentName);
        if (application.isPresent()) {
            return new ApiResponse(
                    HttpStatus.OK.value(),
                    "Application retrieved successfully",
                    application
            );
        }
        return new ApiResponse(
                HttpStatus.NOT_FOUND.value(),
                "Application not found with student name :: " + studentName,
                null
        );
    }
}
