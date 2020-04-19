package com.example.eceapp.businessLayer;


import com.example.eceapp.TemplateClass;
import com.example.eceapp.dataLayer.TemplateClassDL;

import java.util.ArrayList;

public class TemplateClassBL {

    private TemplateClassDL templateClassDL = TemplateClassDL.getInstance();


    public ArrayList<TemplateClass> getActiveStudentsTemplates() {
        return templateClassDL.fetchActiveStudentsTemplates();
    }


    public ArrayList<TemplateClass> getActiveTeachersTemplates() {
        return templateClassDL.fetchActiveTeachersTemplates();
    }

    public ArrayList<TemplateClass> getAllStudentsTemplates() {
        return templateClassDL.fetchAllStudentsTemplates();
    }

    public ArrayList<TemplateClass> getAllTeachersTemplates() {
        return templateClassDL.fetchAllTeachersTemplates();
    }

    public TemplateClass getTemplateById(int templateId) {
        return templateClassDL.fetchTemplateById(templateId);
    }

    public TemplateClass getTemplateByDescription(String tempDesc) {
        return templateClassDL.fetchTemplateByDescription(tempDesc);
    }

    public Boolean saveNewTemplate(TemplateClass template) {
        return templateClassDL.addTemplate(template);
    }

}
