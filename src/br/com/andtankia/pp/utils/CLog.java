package br.com.andtankia.pp.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author andrew
 */
public class CLog {

    private static Logger logger;

    public static Logger getCLog(String file) {
        if (logger == null) {
            logger = Logger.getLogger("CLOG");
        } else {
            return logger;
        }
        LogManager.getLogManager().reset();
        File f = new File(file);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        FileHandler fh = null;
        try {
            fh = new FileHandler(file);
        } catch (IOException ex) {
            Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        ConsoleHandler ch = new ConsoleHandler();
        MyFormatter sf = new MyFormatter();
        fh.setFormatter(sf);
        ch.setFormatter(sf);
        logger.addHandler(fh);
        logger.addHandler(ch);

        return logger;
    }

    public static Logger getCLog() {
        return getCLog("anything");
    }

}
