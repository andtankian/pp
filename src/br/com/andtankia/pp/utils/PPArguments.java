package br.com.andtankia.pp.utils;

import com.beust.jcommander.Parameter;

/**
 *
 * @author andrew
 */
public class PPArguments {
    
    @Parameter(names = {"--verbose", "-b"}, description = "Verbose options defines if details about downloading and processing will be showed and logged")
    private boolean verbose = false;
    
    @Parameter(names = {"--url", "-u"}, description = "URL where program will get all contents.")
    private String url;
    
    @Parameter(names={"--logfile", "-l"}, description = "Path to the log file to see what happend while processing")
    private String logFile;
    
    @Parameter(names = {"-v", "--version"}, description = "Current version of the program")
    private boolean version = false;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }
    
}
