package gov.goias.sat2.controllers;

import gov.goias.excecao.NaoEncontradoException;
import gov.goias.sat2.representation.DataTableResponse;
import gov.goias.sat2.services.AlunoService;
import gov.goias.sat2.view.model.Aluno;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.*;

@Controller
@Path("/aluno")
public class AlunoController {

    private static final Logger LOGGER = Logger.getLogger(AlunoController.class);

    @Autowired
    private AlunoService service;

    @Context
    protected HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response incluir(@FormParam("id") final Long id,
                            @NotNull @FormParam("nome") final String nome,
                            @NotNull @FormParam("email") final String email,
                            @NotNull @FormParam("sexo") final String sexo,
                            @FormParam("situacao") final Boolean situacao) throws ParseException {

        Aluno aluno = new Aluno(id, nome, email , null, sexo, situacao);
        if (id != null) {
            service.salvar(aluno.toEntity());
        } else {
            aluno = aluno.from(service.salvar(aluno.toEntity()));
        }
        return Response.status(Response.Status.SEE_OTHER).header("Location", String.format("aluno/%s", aluno.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") final Long id) {
        service.remover(id);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    public Aluno obter(@PathParam("id") final Long id) {
        return Aluno.from(service.obterPorId(id).orElseThrow(() -> new NaoEncontradoException()));
    }

    @GET
    public List<Aluno> listar() {
        return null; //return service.listarPaginado();
    }

    @GET
    @Path("/paginar")
    @Produces({DataTableResponse.JSON})
    public Response list(@QueryParam("draw")   final Integer draw,
                         @QueryParam("start")  final Integer start,
                         @QueryParam("length") final Integer length,
                         @QueryParam("search[value]") final String searchValue,
                         @QueryParam("columns[0][search][value]") final Long id,
                         @QueryParam("columns[1][search][value]") final String nome,
                         @QueryParam("columns[2][search][value]") final String email,
                         @QueryParam("order[0][column]") final Integer ordem,
                         @QueryParam("order[0][dir]") final String ordemDir) {

        final DataTableResponse dtr = new DataTableResponse();
        final List<Map<String, String>> res = new ArrayList<>();
        dtr.setDraw(draw);
        final String[] columns = new String[]{"id", "nome","email"};
        try {
            final Integer qtTotal = new Long(service.contarTodos()).intValue();
            final Map<String, String> searchParams = new HashMap<>();
            if (!searchValue.isEmpty()) {
                searchParams.put(columns[1], searchValue);
            }
            final Integer page = new Double(Math.ceil(start / length)).intValue();

            final PageRequest pr = new PageRequest(page, length,
                    new Sort(new Sort.Order(Sort.Direction.fromString(ordemDir), columns[ordem])));

            final Page<Aluno> list = service.listarPaginado(id, nome, email, pr).map(a -> Aluno.from(a)) ;

            final Integer qtFiltrada = new Long(list.getTotalElements()).intValue();
            if (qtFiltrada > 0) {
                list.forEach(a -> res.add(a.asMapofValues(
                        (Object v) -> String.format("row_%s", v),
                        "DT_RowId",
                        "id",
                        columns
                )));
            }
            dtr.setRecordsFiltered(qtFiltrada);
            dtr.setData(res);
            dtr.setRecordsTotal(qtTotal);
        } catch (Exception e) {
            LOGGER.error(e);
            dtr.setError(e.getMessage());
        }
        return Response.status(Response.Status.OK).entity(dtr).build();
    }

}