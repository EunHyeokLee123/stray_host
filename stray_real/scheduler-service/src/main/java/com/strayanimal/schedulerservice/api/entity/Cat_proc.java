package com.strayanimal.schedulerservice.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cat_processing_table")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Cat_proc {

    @Id
    private String desertion_no;

    // Insert
    // Update
    // Delete
    @Column
    private String status;

    // Pending
    // Success
    // Failed
    @Column
    private String job_type;

}
