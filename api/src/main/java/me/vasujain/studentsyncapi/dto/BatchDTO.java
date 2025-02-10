package me.vasujain.studentsyncapi.dto;

// Table batches {
//  id uuid [primary key]
//  department_id uuid [not null]
//  semester_id uuid [not null]
//  name varchar(100) [not null]
//  schedule_info text [note: 'JSON containing class schedule']
//  capacity int [not null]
//  coordinator_id uuid [ref: > users.id, note: 'Teacher who coordinates this batch']
//  created_at timestamp
//  updated_at timestamp
//}

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BatchDTO {
    private UUID departmentId;
    private UUID semesterId;
    private String name;
    private String scheduleInfo;
    private Integer capacity;
    private UUID coordinatorId;
}
