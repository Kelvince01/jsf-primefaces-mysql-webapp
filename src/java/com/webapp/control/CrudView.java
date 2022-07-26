/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapp.control;

import com.webapp.entity.Employees;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;

/**
 *
 * @author kelvi
 */
@Named(value = "employeeBean")
@Dependent 
public class CrudView implements Serializable {
    
    private List<Employees> employees;

    private Employees selectedEmployee;

    private List<Employees> selectedEmployees;

    @Inject
    private AbstractFacade<Employees> employeeService;

    /**
     * Creates a new instance of EmployeeBean
     */
    public CrudView() {
    }
    
    @PostConstruct
    public void init() {
        this.employees = this.employeeService.findAll();
    }

    public List<Employees> getEmployees() {
        return employees;
    }

    public Employees getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employees selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public List<Employees> getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(List<Employees> selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public void openNew() {
        this.selectedEmployee = new Employees();
    }

    public void saveEmployee() {
        if (this.selectedEmployee.getId() == null) {
            //this.selectedEmployee.setId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9));
            this.employees.add(this.selectedEmployee);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Added"));
        }
        else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageEmployeeDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
    }

    public void deleteEmployee() {
        this.employees.remove(this.selectedEmployee);
        this.selectedEmployee = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employee Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
    }

    public String getDeleteButtonMessage() {
        if (hasSelectedEmployees()) {
            int size = this.selectedEmployees.size();
            return size > 1 ? size + " employees selected" : "1 employee selected";
        }

        return "Delete";
    }

    public boolean hasSelectedEmployees() {
        return this.selectedEmployees != null && !this.selectedEmployees.isEmpty();
    }

    public void deleteSelectedEmployees() {
        this.employees.removeAll(this.selectedEmployees);
        this.selectedEmployees = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Employees Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-employees");
        PrimeFaces.current().executeScript("PF('dtEmployees').clearFilters()");
    }
    
}
