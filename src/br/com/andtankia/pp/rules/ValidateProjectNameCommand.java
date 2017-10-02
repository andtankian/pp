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
        StringBuilder sb = new StringBuilder();
        if(pn == null || pn.isEmpty()){
            pn = String.valueOf(Calendar.getInstance().getTimeInMillis());
            CLog.getCLog(pn + ".log").info("None project name was explicitly defined, program will create one...");
        }
        
        ppa.setName(pn);
        File f = new File(pn);
        f.mkdirs();
        CLog.getCLog(pn + ".log").info(sb.append("Project directory created: ").append(pn).toString());
        fc.getPpholder().getProject().setLocation(pn);
        
        String pbi = ppa.getIndex();
        if(pbi == null || pbi.isEmpty()){
            pbi = ppa.getUrl();
        }
        fc.getPpholder().getProject().setBaseIndex(pbi);
        sb.delete(0, sb.length());
        CLog.getCLog().info(sb.append("Program assumes base index location is ").append(pbi).toString());
    }
    
}
