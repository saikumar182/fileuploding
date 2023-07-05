package com.example.fileUploading.controller;

import com.example.fileUploading.entity.Employee;
import com.example.fileUploading.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
    @RestController
    public class EmployeeController {
        @Autowired
        private EmployeeRepo employeeRepo;

        public EmployeeController(EmployeeRepo employeeRepo) {
            this.employeeRepo = employeeRepo;
        }

        @PostMapping("/fileupload")
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
                List<Employee> employees = new ArrayList<>();
                String line;
                boolean isFirstLine = true;
                while ((line = bufferedReader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // Skip the first line
                    }
                    String[] data = line.split(",");
                    System.out.println(Arrays.toString(data)); // Add this line for debugging
                    if (data.length == 4) {
                        Employee employee = new Employee();
                        employee.setId(Integer.parseInt(data[0]));
                        employee.setFirstName(data[1]);
                        employee.setLastName(data[2]);
                        employee.setEmail(data[3]);
                        employees.add(employee);
                    }
                }
                employeeRepo.saveAll(employees);
                return ResponseEntity.status(HttpStatus.OK).body("fileUploaded successfully");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error uploading ");
            }
        }
    }

