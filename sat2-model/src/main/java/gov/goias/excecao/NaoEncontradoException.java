package gov.goias.excecao;


public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(){
        super("Not Found");
    }

}
