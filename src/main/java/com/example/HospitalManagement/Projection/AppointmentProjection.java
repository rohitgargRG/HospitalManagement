package com.example.HospitalManagement.Projection;

import com.example.HospitalManagement.Entity.Appointment;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "appointmentView", types = Appointment.class)
public interface AppointmentProjection {
    Integer getAppointmentId();
    String getExaminationRoom();
    Date getStarto();
    Date getEndo();
    PatientInfo getPatient();
    interface PatientInfo {
        String getName();
    }
}