package gov.goias.excecao;

public class InfraException extends  RuntimeException {

    public InfraException(String message) {
        super(message);
    }

    public InfraException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfraException(Throwable cause) {
        super(cause);
    }

    protected InfraException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static  InfraException raise(String message){
        return new InfraException(message);
    }

    public static InfraException raise(String message, Throwable cause){
        return  new InfraException(message,cause);
    }

    public static InfraException raise(Throwable cause){
        return new InfraException(cause);
    }

}
