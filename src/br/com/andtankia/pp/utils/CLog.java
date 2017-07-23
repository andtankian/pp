
package br.com.andtankia.pp.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author andrew
 */
public class CLog {
    
    private static Logger logger;
    
    public static Logger getCLog(String file){
        if(logger == null) logger = Logger.getLogger("CLOG");
        else return logger;
        File f = new File(file);
        if(!f.exists()) try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileHandler fh = null;
        try {
            fh = new FileHandler(file);
        } catch (IOException ex) {
            Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        SimpleFormatter sf = new SimpleFormatter();
        fh.setFormatter(sf);
        logger.addHandler(fh);
        
        return logger;
    }
    public static Logger getCLog(){
        return getCLog("anything");
    }
}
