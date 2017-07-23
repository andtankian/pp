package br.com.andtankia.pp.rules;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrew
 */
public class ValidateProjectNameCommand implements ICommand{

    @Override
    public void exe(FlowContainer fc) {
        PPArguments ppa = fc.getPpholder().getPpa();
        
        if(ppa.getName() == null || ppa.getName().isEmpty()){
        }
    }
    
}
