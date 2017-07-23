package br.com.andtankian.pp;

import br.com.andtankia.pp.control.Facade;
import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;
import com.beust.jcommander.JCommander;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andrew
 */
public class PP {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Logger l = Logger.getLogger("CLOG");
        l.info("Starting PP tool..");
        PPArguments ppa = new PPArguments();
        l.info("Instantiate ppa arguments");
        /**
         * Let's verify if the arguments are valids
         */
        JCommander.newBuilder().addObject(ppa)
                .build()
                .parse(args);
        l.info("Building and parsing argument list");
        
        
        Facade facade = new Facade(new FlowContainer(ppa));
        
        l.info(facade.process());
        
    }
    
}
