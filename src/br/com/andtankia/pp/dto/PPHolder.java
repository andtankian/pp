package br.com.andtankia.pp.dto;

import br.com.andtankia.pp.domain.Project;
import br.com.andtankia.pp.utils.PPArguments;

/**
 *
 * @author andrew
 */
public class PPHolder {

    public PPHolder(Project project) {
        this.project = project;
    }
    
    
    
    private PPArguments ppa;
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
    public PPArguments getPpa() {
        return ppa;
    }

    public void setPpa(PPArguments ppa) {
        this.ppa = ppa;
    }
}
