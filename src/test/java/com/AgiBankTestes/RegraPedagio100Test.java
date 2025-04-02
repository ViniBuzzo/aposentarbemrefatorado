package com.AgiBankTestes;

import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;
import com.AgiBank.service.ElegibilidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegraPedagio100Test {

    private ElegibilidadeService elegibilidadeService;

    @BeforeEach
    void setUp() {
        elegibilidadeService = new ElegibilidadeService();
    }

    @Test
    void testUsuarioElegivelPedagio100() {
        // ✅ Usuário masculino com idade mínima, mas sem tempo de contribuição suficiente
        Usuario usuario = new Usuario("Carlos", "10/10/1959", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 63);

        // ✅ Criando contribuições fictícias até a reforma (apenas 25 anos de contribuição)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(1, usuario.getId(), 4500.00, LocalDate.of(1994, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio100(usuario, contribuicoes);
        assertTrue(elegivel, "Usuário deveria estar elegível para o Pedágio 100%.");
    }

    @Test
    void testUsuarioNaoElegivelPorIdade() {
        // ❌ Usuário masculino que NÃO tinha a idade mínima de 60 anos na reforma
        Usuario usuario = new Usuario("João", "05/05/1965", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 63);

        // ✅ Contribuições suficientes, mas ainda não tinha a idade mínima
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(2, usuario.getId(), 5000.00, LocalDate.of(1985, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio100(usuario, contribuicoes);
        assertFalse(elegivel, "Usuário não deveria estar elegível, pois não tinha a idade mínima.");
    }

    @Test
    void testUsuarioNaoElegivelPorTempoContribuicao() {
        // ❌ Usuário feminino que já tinha atingido os 30 anos de contribuição antes da reforma
        Usuario usuario = new Usuario("Maria", "15/08/1960", Usuario.Genero.FEMININO, Usuario.Profissao.GERAL, 63);

        // ✅ Criando contribuições suficientes (30 anos completos antes de 2019)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(3, usuario.getId(), 6000.00, LocalDate.of(1989, 8, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio100(usuario, contribuicoes);
        assertFalse(elegivel, "Usuário não deveria estar elegível, pois já tinha o tempo mínimo de contribuição.");
    }

    @Test
    void testProfessorElegivelPedagio100() {
        // ✅ Professora que já tinha idade mínima de 52 anos, mas não tempo suficiente (só 20 anos)
        Usuario usuario = new Usuario("Ana", "20/05/1967", Usuario.Genero.FEMININO, Usuario.Profissao.PROFESSOR, 62);

        // ✅ Criando contribuições fictícias (20 anos apenas)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(4, usuario.getId(), 5500.00, LocalDate.of(1999, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio100(usuario, contribuicoes);
        assertTrue(elegivel, "Professora deveria estar elegível para o Pedágio 100%.");
    }

    @Test
    void testProfessorNaoElegivelPedagio100() {
        // ❌ Professor que não tinha idade mínima (tinha apenas 50 anos em 2019)
        Usuario usuario = new Usuario("Pedro", "10/06/1969", Usuario.Genero.MASCULINO, Usuario.Profissao.PROFESSOR, 55);

        // ✅ Criando contribuições fictícias (27 anos contribuídos)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(5, usuario.getId(), 5000.00, LocalDate.of(1992, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio100(usuario, contribuicoes);
        assertFalse(elegivel, "Professor não deveria estar elegível, pois não tinha idade mínima.");
    }
}
