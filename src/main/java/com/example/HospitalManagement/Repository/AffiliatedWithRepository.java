package com.example.HospitalManagement.Repository;




import com.example.HospitalManagement.Entity.AffiliatedWith;
import com.example.HospitalManagement.Entity.AffiliatedWithId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "affiliations", collectionResourceRel = "affiliations")
public interface AffiliatedWithRepository extends JpaRepository<AffiliatedWith, AffiliatedWithId> {

    // Custom Search 1: Find all departments a specific physician is affiliated with
    Page<AffiliatedWith> findByPhysician(@Param("physicianId") Integer physicianId, Pageable pageable);

    // Custom Search 2: Find all physicians working in a specific department
    Page<AffiliatedWith> findByDepartment(@Param("departmentId") Integer departmentId, Pageable pageable);
    
    // Custom Search 3: Find ONLY the primary affiliation for a specific doctor
    Page<AffiliatedWith> findByPhysicianAndPrimaryAffiliationTrue(@Param("physicianId") Integer physicianId, Pageable pageable);
}
