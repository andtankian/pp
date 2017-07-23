package br.com.andtankia.pp.rules;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;
import java.io.File;
import java.util.Calendar;

/**
 *
 * @author andrew
 */
public class ValidateProjectNameCommand implements ICommand{

    @Override
    public void exe(FlowContainer fc) {
        PPArguments ppa = fc.getPpholder().getPpa();
        String pn = ppa.getName();
        
        if(pn == null || pn.isEmpty()){
            pn = String.valueOf(Calendar.getInstance().getTimeInMillis());
            CLog.getCLog(pn + ".log").info("None project name was explicitly defined, program will create one...");
        }
        
        ppa.setName(pn);
        
        File f = new File(pn);
        f.mkdirs();
        CLog.getCLog(pn + ".log").info("Project directory created ");
    }
    
}
