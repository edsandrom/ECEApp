package com.example.eceapp;

public class TemplateClass {

    private int templateId;
    private boolean student_allowed, teacher_allowed, active;
    private String location, description;

    public TemplateClass() {
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public boolean isStudent_allowed() {
        return student_allowed;
    }

    public void setStudent_allowed(boolean student_allowed) {
        this.student_allowed = student_allowed;
    }

    public boolean isTeacher_allowed() {
        return teacher_allowed;
    }

    public void setTeacher_allowed(boolean teacher_allowed) {
        this.teacher_allowed = teacher_allowed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
