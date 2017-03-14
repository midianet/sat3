package gov.goias.sat2.services;

import com.google.common.base.Strings;
import gov.goias.excecao.InfraException;
import gov.goias.sat2.entities.Aluno;
import gov.goias.sat2.repositories.AlunoRepository;
import javaslang.collection.List;
import javaslang.control.Try;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.Serializable;
import java.util.Optional;

import static gov.goias.sat2.entities.Aluno.*;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Created by thiago-rs on 4/11/16.
 */
@Service
public class AlunoService{
    private final Logger log = Logger.getLogger(getClass());

    @Autowired
    private AlunoRepository repository;

    public Page<Aluno> listarPaginado(final Long id, final String nome, final String email, final PageRequest page) {
        List<Specification<Aluno>> specs = List.empty();
        specs = id != null && id > 0? specs.append(id(id)) : specs;
        specs = Strings.isNullOrEmpty(nome)? specs : specs.append(nomeIniciando(nome));
        specs = Strings.isNullOrEmpty(email)? specs : specs.append(comEmail(email));
        final boolean noSpec = specs.isEmpty();
        final Specification<Aluno> spec = noSpec? null : specs.reduce((a1, a2) -> where(a1).and(a2));
        return Try.of(() -> noSpec? repository.findAll(page) : repository.findAll(spec, page))
                .onFailure(e -> new InfraException(e))
                .get();
    }

    public Optional<Aluno> obterPorId(final Long id) {
        return Try.of(() -> repository.findOne(id))
                .map(e -> Optional.ofNullable(e))
                .onFailure(InfraException::raise)
                .get();
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public void remover(final Long id) {
        try {
            obterPorId(id).ifPresent(aluno -> repository.delete(aluno));
        } catch (Exception e) {
            throw new InfraException(e);
        }
    }

    @Transactional(readOnly = false, rollbackFor = RuntimeException.class)
    public Aluno salvar(final Aluno aluno) {
        return Try.of(() -> repository.save(aluno))
                .onFailure(InfraException::raise)
                .get();
    }

    public Long contarTodos() {
        return Try.of(() -> repository.count())
                .onFailure(InfraException::raise)
                .get();
    }

}