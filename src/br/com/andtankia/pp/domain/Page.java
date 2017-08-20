package br.com.andtankia.pp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Page extends GenericResource{

    public Page() {
        
        jss = new ArrayList();
        csss = new ArrayList();
        images = new ArrayList();
    }
    
    
    
    private List jss;
    private List csss;
    private List images;

    public List getJss() {
        return jss;
    }

    public void setJss(List jss) {
        this.jss = jss;
    }

    public List getCsss() {
        return csss;
    }

    public void setCsss(List csss) {
        this.csss = csss;
    }

    public List getImages() {
        return images;
    }

    public void setImages(List images) {
        this.images = images;
    }
    
}
