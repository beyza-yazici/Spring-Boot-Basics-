package com.workintech.spring17challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

    private String name;
    private Integer credit;
    private Grade grade;
    private Integer id;
}
