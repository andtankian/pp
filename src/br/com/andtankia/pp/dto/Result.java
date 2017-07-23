package br.com.andtankia.pp.dto;

/**
 *
 * @author andrew
 */
public class Result {

    public Result() {        
        status = "ok";
        this.message = new Message();
    }
    
    

    private Message message;
    private String status;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (status.equals("ok")) {
            sb.append("Concluded successfully.\n")
                    .append("message: ").append(message.getText()).append("\n")
                    .append("status: ").append(status);
        } else {
            sb.append("Not concluded.\n")
                     .append("message: ").append(message.getError()).append("\n")
                    .append("status: ").append(status);
        }
        
        return sb.toString();
    }

}
