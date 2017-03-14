package gov.goias.sat2.controllers;

import gov.goias.sat2.representation.ToolbarRep;
import gov.goias.sat2.representation.UsuarioRep;
import javaslang.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;


@Controller
@Path("toolbar")
public class ToolbarQueries {

    @Context
    private HttpServletRequest req;

    @GET
    public ToolbarRep toolbar() throws IOException {
        UsuarioRep u = new UsuarioRep();
        u.setNomeUsuario("Fake");
        return new ToolbarRep(u);
    }

}
