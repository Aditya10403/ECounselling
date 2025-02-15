package com.ECounselling.service;

import com.ECounselling.model.AllocationResult;
import com.ECounselling.model.Application;
import com.ECounselling.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<AllocationResult> allocateDepartments() {
        List<Application> applications = applicationRepository.findAll();
        applications.sort(Comparator.comparing(Application::getErank)); 
        List<AllocationResult> results = new ArrayList<>();
        Set<String> allocatedDepartments = new HashSet<>();

        for (Application app : applications) {
            String firstPref = app.getFirstPreference().get("departmentName") + " - " + app.getFirstPreference().get("collegeName");
            String secondPref = app.getSecondPreference().get("departmentName") + " - " + app.getSecondPreference().get("collegeName");
            String thirdPref = app.getThirdPreference().get("departmentName") + " - " + app.getThirdPreference().get("collegeName");

            if (!allocatedDepartments.contains(firstPref)) {
                results.add(new AllocationResult(app.getStudentName(), app.getFirstPreference().get("departmentName"), app.getFirstPreference().get("collegeName")));
                allocatedDepartments.add(firstPref);
            } else if (!allocatedDepartments.contains(secondPref)) {
                results.add(new AllocationResult(app.getStudentName(), app.getSecondPreference().get("departmentName"), app.getSecondPreference().get("collegeName")));
                allocatedDepartments.add(secondPref);
            } else if (!allocatedDepartments.contains(thirdPref)) {
                results.add(new AllocationResult(app.getStudentName(), app.getThirdPreference().get("departmentName"), app.getThirdPreference().get("collegeName")));
                allocatedDepartments.add(thirdPref);
            } else {
                results.add(new AllocationResult(app.getStudentName(), "Not Allocated", ""));
            }
        }
        return results;
    }
}
