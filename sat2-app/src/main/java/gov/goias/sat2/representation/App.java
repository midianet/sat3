package gov.goias.sat2.representation;

import gov.goias.sat2.entities.ProtocoloAuth;
import gov.goias.sat2.entities.Sistema;
import gov.goias.sat2.entities.StatusSistema;
import gov.goias.sat2.util.DozerUtil;
import lombok.Data;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;

import static org.dozer.loader.api.TypeMappingOptions.mapId;
import static org.dozer.loader.api.TypeMappingOptions.mapNull;

/**
 * Created by thiago-rs on 4/11/16.
 */
@Data
public class App {
    private Integer id;
    private String sigla;
    private String descricao;
    private String conexao;
    private String mensagem;
    private String logotipo;
    private StatusSistema status;
    private String nomeAplicacao;
    private ProtocoloAuth protocolo;

    public static BeanMappingBuilder getMappingBuilder(){
        return new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(Sistema.class,
                        App.class,
                        TypeMappingOptions.oneWay(),
                        mapId("A"),
                        mapNull(true)
                );
            }
        };
    }

    public static App from(final Sistema s){
        return DozerUtil.getMapper().map(s,App.class);
    }

    public Sistema toSistema(){
        return DozerUtil.getMapper().map(this,Sistema.class);
    }

}