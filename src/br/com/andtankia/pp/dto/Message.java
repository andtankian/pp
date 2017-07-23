package br.com.andtankia.pp.dto;

/**
 *
 * @author andrew
 */
public class Message {
    
   private String error;
   private String text;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
