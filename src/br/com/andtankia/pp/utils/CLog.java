
package br.com.andtankia.pp.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author andrew
 */
public class CLog {
    
    private static Logger logger;
    
    public static Logger getCLog(String file) throws IOException{
        if(logger == null) logger = Logger.getLogger("CLOG");
        else return logger;
        File f = new File(file);
        if(!f.exists()) f.createNewFile();
        FileHandler fh = new FileHandler(file);
        SimpleFormatter sf = new SimpleFormatter();
        fh.setFormatter(sf);
        logger.addHandler(fh);
        
        return logger;
    }
    public static Logger getCLog() throws IOException{
        return getCLog("clog");
    }
}
