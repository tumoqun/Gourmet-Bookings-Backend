package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salary_scales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryScale {

    @Id
    @Column(name = "scale_key", length = 100)
    private String scaleKey;

    @Column(name = "hourly_salary", nullable = false)
    private Integer hourlySalary;
}
