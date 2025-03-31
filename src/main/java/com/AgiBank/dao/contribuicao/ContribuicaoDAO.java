package com.AgiBank.dao.contribuicao;

import com.AgiBank.model.Contribuicao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ContribuicaoDAO {
    void adicionar(Contribuicao contribuicao);

    Contribuicao buscarPorId(int idContribuicao);

    List<Contribuicao> buscarPorUsuario(int idUsuario);

    void atualizar(Contribuicao contribuicao);

    void remover(int idContribuicao);
}
