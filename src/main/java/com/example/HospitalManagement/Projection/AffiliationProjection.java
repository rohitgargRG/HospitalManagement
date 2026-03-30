package com.example.HospitalManagement.Projection;





import com.example.HospitalManagement.Entity.AffiliatedWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "fullAffiliation", types = { AffiliatedWith.class })
public interface AffiliationProjection {
    
    // 1. This is already flat, so we leave it alone
    Boolean getPrimaryAffiliation();
    
    // 2. Flatten the Physician Data
    @Value("#{target.physicianEntity.name}")
    String getDoctorName();
    
    @Value("#{target.physicianEntity.position}")
    String getDoctorPosition();

    // 3. Flatten the Department Data
    @Value("#{target.departmentEntity.name}")
    String getDepartmentName();

    // 4. THE DEEP DIVE: Get the Head of the Department!
    // We use "?." (Safe Navigation) just in case a department doesn't have a head yet, 
    // so it returns null instead of crashing your API.
    @Value("#{target.departmentEntity?.head?.name}")
    String getDepartmentHeadName();
}