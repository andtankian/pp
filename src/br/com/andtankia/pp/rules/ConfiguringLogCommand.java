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
public class ConfiguringLogCommand implements ICommand{

    @Override
    public void exe(FlowContainer fc) {
        PPArguments ppa = fc.getPpholder().getPpa();
        Logger l;
        if(ppa.getLogFile() == null || ppa.getLogFile().isEmpty()) {
            l = CLog.getCLog();
                l.info(new StringBuilder("Log file was created in the current working directory which is ").append(System.getProperty("user.dir")).toString());          
            
        } else {
            l = CLog.getCLog(ppa.getLogFile());
                l.info(new StringBuilder("Log file was created on ").append(ppa.getLogFile()).toString());
        }
         
    }
    
}
