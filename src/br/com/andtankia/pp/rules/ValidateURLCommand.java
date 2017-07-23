package br.com.andtankia.pp.rules;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;

/**
 *
 * @author andrew
 */
public class ValidateURLCommand implements ICommand{

    @Override
    public void exe(FlowContainer fc) {
        
       PPArguments ppa = fc.getPpholder().getPpa();
       if(ppa.getUrl() == null || ppa.getUrl().isEmpty()){
           fc.getResult().setStatus("fail");
           fc.getResult().getMessage().setError("invalid url");
           fc.setProceed(false);
       }
    }
    
}
