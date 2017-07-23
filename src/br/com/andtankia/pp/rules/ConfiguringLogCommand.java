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
            try {
                l = CLog.getCLog();
            } catch (IOException ex) {
                Logger.getLogger(ConfiguringLogCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                l = CLog.getCLog(ppa.getLogFile());
            } catch (IOException ex) {
                Logger.getLogger(ConfiguringLogCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         
    }
    
}
