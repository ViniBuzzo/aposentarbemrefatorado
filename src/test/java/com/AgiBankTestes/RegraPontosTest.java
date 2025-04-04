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
    void testAposentadoriaHomemComTempoSuficiente() {
        Usuario usuario = new Usuario("João", "10/10/1970", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 65);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(1, usuario.getId(), 5000.0, LocalDate.of(1989, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 João - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }

    @Test
    void testAposentadoriaMulherComTempoMinimo() {
        Usuario usuario = new Usuario("Maria", "20/05/1975", Usuario.Genero.FEMININO, Usuario.Profissao.GERAL, 62);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(2, usuario.getId(), 3000.0, LocalDate.of(2005, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 Maria - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }

    @Test
    void testUsuarioAindaNaoTemPontosSuficientes() {
        Usuario usuario = new Usuario("Carlos", "15/03/1985", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 60);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(3, usuario.getId(), 4000.0, LocalDate.of(2010, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟡 Carlos - Resultado:");
        System.out.println(resultado);

        assertFalse((boolean) resultado.get("elegivel"));
        assertTrue((int) resultado.get("idadeElegivel") > usuario.getIdadeAposentadoriaDesejada());
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }

    @Test
    void testUsuarioProfessorComTempoEspecial() {
        Usuario usuario = new Usuario("Paulo", "01/01/1970", Usuario.Genero.MASCULINO, Usuario.Profissao.PROFESSOR, 60);

        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(4, usuario.getId(), 3500.0, LocalDate.of(1995, 1, 1), LocalDate.of(2024, 12, 31))
        );

        Map<String, Object> resultado = regraPontos.calcularRegraPontos(usuario, contribuicoes, usuario.getIdadeAposentadoriaDesejada());

        System.out.println("\n🟢 Paulo (Professor) - Resultado:");
        System.out.println(resultado);

        assertTrue((boolean) resultado.get("elegivel"));
        assertTrue((double) resultado.get("valorEstimado") >= 1518.0);
    }
}
