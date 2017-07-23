package br.com.andtankia.pp.rules;

import br.com.andtankia.pp.dto.FlowContainer;
import br.com.andtankia.pp.dto.Result;
import br.com.andtankia.pp.utils.PPArguments;

/**
 *
 * @author andrew
 */
public interface ICommand {
    
    public void exe(FlowContainer fc);
}
