package com.example.HospitalManagement.Repository;

import com.example.HospitalManagement.Entity.Department;
import com.example.HospitalManagement.Projection.DepartmentProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "departments", collectionResourceRel = "departments",excerptProjection = DepartmentProjection.class)
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

  
    Page<Department> findByHead_EmployeeId(Integer headId, Pageable pageable);

     Page<Department> findByName(String name,Pageable pageable);

     

    
    boolean existsByHead_EmployeeId(Integer physicianId);
}

