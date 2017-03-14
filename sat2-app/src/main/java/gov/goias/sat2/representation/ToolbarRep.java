package gov.goias.sat2.representation;

import lombok.Data;
import java.io.IOException;

@Data
public class ToolbarRep {

    public ToolbarRep() throws IOException {

    }

    public ToolbarRep(UsuarioRep usuario) throws IOException {
        this.usuario = usuario;
    }

    private UsuarioRep usuario;
}
