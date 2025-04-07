package com.AgiBankTestes;

import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;
import com.AgiBank.service.ElegibilidadeService;
import com.AgiBank.service.RegraPontos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RegraPontosTest {

    private RegraPontos regraPontos;
    private ElegibilidadeService elegibilidadeService;

    @BeforeEach
    void setUp() {
        ElegibilidadeService elegibilidadeService = new ElegibilidadeService();
        regraPontos = new RegraPontos(elegibilidadeService);
    }

    @Test
    void testSalarioMedioAbaixoDoMinimo() {
        Usuario usuario = new Usuario("Ana", "01/01/1965", Usuario.Genero.FEMININO, Usuario.Profissao.GERAL, 62);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(5, usuario.getId(), 800.0, LocalDate.of(1990, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 Ana (Salário baixo) - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertEquals(1518.0, (double) resultado.get("valorEstimado")); // deve aplicar piso mínimo
    }

    @Test
    void testUsuarioJovemComPoucoTempo() {
        Usuario usuario = new Usuario("Bruno", "01/01/1995", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 35);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(6, usuario.getId(), 2500.0, LocalDate.of(2020, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟡 Bruno (Jovem) - Resultado:");
        System.out.println(resultado);

        assertFalse((boolean) resultado.get("elegivel"));
        assertTrue((int) resultado.get("idadeElegivel") > usuario.getIdadeAposentadoriaDesejada());
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }
    @Test
    void testProfessoraCom25AnosContribuicao() {
        Usuario usuario = new Usuario("Lúcia", "01/01/1975", Usuario.Genero.FEMININO, Usuario.Profissao.PROFESSOR, 55);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(7, usuario.getId(), 3200.0, LocalDate.of(1999, 1, 1), LocalDate.of(2024, 1, 1))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 Lúcia (Professora) - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }
    @Test
    void testHomemElegivelComBoasContribuicoes() {
        Usuario usuario = new Usuario("Fernando", "15/06/1962", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 65);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(8, usuario.getId(), 4500.0, LocalDate.of(1990, 1, 1), LocalDate.of(2005, 12, 31)),
                new Contribuicao(8, usuario.getId(), 6000.0, LocalDate.of(2006, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 Fernando (Elegível com boas contribuições) - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }
    @Test
    void testHomemNaoElegivelComPoucasContribuicoes() {
        Usuario usuario = new Usuario("Eduardo", "01/01/1980", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 55);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(9, usuario.getId(), 3500.0, LocalDate.of(2015, 1, 1), LocalDate.of(2020, 12, 31)),
                new Contribuicao(9, usuario.getId(), 4000.0, LocalDate.of(2021, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟡 Eduardo (Não elegível com boas contribuições) - Resultado:");
        System.out.println(resultado);

        assertFalse((boolean) resultado.get("elegivel"));
        assertTrue((int) resultado.get("idadeElegivel") > usuario.getIdadeAposentadoriaDesejada());
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }

}