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
    
    @Parameter(names = {"--name", "-n"}, description = "Name of the project that will be cloned")
    private String name;
    
    @Parameter(names = {"-i", "--index"}, description = "Base index address to program try to guess where assets are located")
    private String index;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    
}
