package br.com.andtankia.pp.rules;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.PPArguments;
import br.com.andtankian.pp.PP;

/**
 *
 * @author andrew
 */
public class VerifyVersionCommand implements ICommand{

    @Override
    public void exe(FlowContainer fc) {
        PPArguments ppa = fc.getPpholder().getPpa();
        
        if(ppa.isVersion()) {
            fc.setProceed(false);
            fc.getResult().setStatus("ok");
            fc.getResult().getMessage().setText(PP.VERSION);
        }
    }
    
}
