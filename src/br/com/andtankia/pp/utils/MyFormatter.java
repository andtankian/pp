package br.com.andtankia.pp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class MyFormatter extends Formatter {
    
    // Create a DateFormat to format the logger timestamp.
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy 'às' hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(record.getLevel()).append(" - ");
        sb.append(df.format(new Date(record.getMillis()))).append(":");
        sb.append(record.getMessage()).append("\n");
        return sb.toString();
    }
}
