package br.com.andtankian.pp;

import br.com.andtankia.pp.control.Facade;
import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.utils.CLog;
import br.com.andtankia.pp.utils.PPArguments;
import com.beust.jcommander.JCommander;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 * @author andrew
 */
public class PP {
    /**
     * @param args the command line arguments
     */
    
    public final static String VERSION = "Current version: 1.1.0";
    public static void main(String[] args) throws IOException {
        PPArguments ppa = new PPArguments();
        /**
         * Let's verify if the arguments are valids
         */
        JCommander.newBuilder().addObject(ppa)
                .build()
                .parse(args);        
        
        Facade facade = new Facade(new FlowContainer(ppa));
        
        facade.process();
        
    }
    
}
