package br.com.andtankia.pp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Project{

    public Project(String name) {
        pages = new ArrayList();
        allcsss = new ArrayList();
        this.name = name;
    }

    public Project() {
        pages = new ArrayList();
        allcsss = new ArrayList();
    }
    
    
    
    private List pages;
    private List allcsss;
    private List allimages;
    private List alljsss;
    private String name;
    private String location;

    public List getPages() {
        return pages;
    }

    public void setPages(List pages) {
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List getAllcsss() {
        return allcsss;
    }

    public void setAllcsss(List allcsss) {
        this.allcsss = allcsss;
    }

    public List getAllimages() {
        return allimages;
    }

    public void setAllimages(List allimages) {
        this.allimages = allimages;
    }

    public List getAlljsss() {
        return alljsss;
    }

    public void setAlljsss(List alljsss) {
        this.alljsss = alljsss;
    }
    
}
