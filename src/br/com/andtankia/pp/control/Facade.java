package br.com.andtankia.pp.control;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.dto.Result;
import br.com.andtankia.pp.rules.ConfiguringLogCommand;
import br.com.andtankia.pp.rules.ICommand;
import br.com.andtankia.pp.rules.ValidateURLCommand;
import br.com.andtankia.pp.rules.VerifyVersionCommand;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrew
 */
public class Facade {

    FlowContainer fc;

    public Facade(FlowContainer fc) {
        this.fc = fc;
        fc.setResult(new Result());
        fc.setProceed(true);
    }

    public String process() {

        try {
            runBefore();
            run();
            runAfter();
        } catch (Exception e) {}

        return fc.getResult().toString();
    }

    private void runBefore() throws Exception{
        List l = new ArrayList();
        l.add(new VerifyVersionCommand());
        l.add(new ConfiguringLogCommand());
        l.add(new ValidateURLCommand());

        for (Object object : l) {
            ((ICommand) object).exe(fc);
            mustProcced();
        }
    }

    private void runAfter() {

    }

    private void run() {
        
    }

    private void mustProcced() throws Exception {
        if (!fc.isProceed()) {
            throw new Exception(fc.getResult().getMessage().getError());
        }
    }

}
