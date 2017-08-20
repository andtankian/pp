package br.com.andtankia.pp.domain;

/**
 *
 * @author andrew
 */
public class GenericResource extends AbstractResource{
    
    private String originalUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
}
