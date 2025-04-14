package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.entity.HighCourseGpa;
import com.workintech.spring17challenge.entity.LowCourseGpa;
import com.workintech.spring17challenge.entity.MediumCourseGpa;
import com.workintech.spring17challenge.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final LowCourseGpa lowCourseGpa;
    private final MediumCourseGpa mediumCourseGpa;
    private final HighCourseGpa highCourseGpa;

    private List<Course> courses = new ArrayList<>();

    @Autowired
    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name) {
        return courses.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course not found", HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {

        courses.stream().anyMatch(c -> c.getName().equals(course.getName()));

        if (course.getCredit() < 0 || course.getCredit() > 4) {
            throw new IllegalArgumentException("Credit must be between 0 and 4.");
        }

        int totalGpa = calculateTotalGpa(course);


        courses.add(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }


    private int calculateTotalGpa(Course course) {
        int totalGpa = course.getGrade().getCoefficient();

        if(course.getCredit() <= 2) {
            return totalGpa * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            return totalGpa * mediumCourseGpa.getGpa();
        } else {
            return totalGpa * highCourseGpa.getGpa();
        }
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        Optional<Course> courseOptional = courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (courseOptional.isPresent()) {
            Course existingCourse = courseOptional.get();
            existingCourse.setName(updatedCourse.getName());
            existingCourse.setCredit(updatedCourse.getCredit());
            existingCourse.setGrade(updatedCourse.getGrade());

            int totalGpa = calculateTotalGpa(existingCourse);

            return existingCourse;
        } else {
            throw new IllegalArgumentException("Course not found");
        }

    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        courses.removeIf(c -> c.getId() == id);
    }

}