package shiro.common;

/**
 * Created by author on 2017/9/15.
 */
public class Response {
    private int status;
    private Object body;
    private String message;

    public Response() {
    }

    public Response(int status) {
        this.status = status;
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(int status, String message, Object body) {
        this.status = status;
        this.body = body;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
