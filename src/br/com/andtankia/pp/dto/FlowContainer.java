package br.com.andtankia.pp.dto;

import br.com.andtankia.pp.domain.Project;
import br.com.andtankia.pp.utils.PPArguments;

/**
 *
 * @author andrew
 */
public class FlowContainer {

    public FlowContainer(PPArguments ppa) {
        this.ppholder = new PPHolder(new Project());
        this.ppholder.setPpa(ppa);
    }
    
    
    
    private Result result;
    private PPHolder ppholder;
    private boolean proceed;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public PPHolder getPpholder() {
        return ppholder;
    }

    public void setPpholder(PPHolder ppholder) {
        this.ppholder = ppholder;
    }

    public boolean isProceed() {
        return proceed;
    }

    public void setProceed(boolean proceed) {
        this.proceed = proceed;
    }
    
}
