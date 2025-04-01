package com.AgiBankTestes;

import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;
import com.AgiBank.service.ElegibilidadeService;
import com.AgiBank.service.RegraPedagio50;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegraPedagio50Test {

    private RegraPedagio50 regraPedagio50;
    private ElegibilidadeService elegibilidadeService;

    @BeforeEach
    void setUp() {
        elegibilidadeService = new ElegibilidadeService();
        regraPedagio50 = new RegraPedagio50(elegibilidadeService);
    }

    @Test
    void testCalculoAposentadoriaPedagio50() {
        // ✅ Criando um usuário masculino que contribuiu antes da reforma
        Usuario usuario = new Usuario("Carlos", "10/10/1969", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 63);

        // ✅ Criando contribuições fictícias desde 1994 até 2019
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(1, usuario.getId(), 4500.00, LocalDate.of(1986, 1, 1), LocalDate.of(2019, 11, 12))
        );

        double expectativaVida = 76.5; // Exemplo IBGE

        double valorAposentadoria = regraPedagio50.calcularAposentadoria(usuario, contribuicoes, expectativaVida);
        boolean isElegivel = elegibilidadeService.isElegivelPedagio50(usuario, contribuicoes);
        double mediacontribuicao = regraPedagio50.calcularAposentadoria(usuario, contribuicoes, expectativaVida);


        // ✅ PRINTAR VALOR PARA DEPURAÇÃO
        System.out.println(isElegivel);
        System.out.println(mediacontribuicao);
        System.out.println("Valor da aposentadoria pelo Pedágio 50%: R$ " + valorAposentadoria);

        // ✅ O valor deve ser maior que 0, pois o usuário atende aos critérios
        assertTrue(valorAposentadoria > 0, "O usuário deveria receber aposentadoria pelo Pedágio 50%");

    }
    @Test
    void testUsuarioElegivelPedagio50() {
        // ✅ Criando um usuário masculino que contribuiu antes da reforma e faltava 1 ano e 6 meses (18 meses)
        Usuario usuario = new Usuario("Carlos", "10/10/1969", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 63);

        // ✅ Criando contribuições fictícias (ele começou a trabalhar em 1986 e contribuiu até 2019)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(1, usuario.getId(), 4500.00, LocalDate.of(1986, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio50(usuario, contribuicoes);
        assertTrue(elegivel, "Usuário deveria estar elegível para o Pedágio 50%");
    }

    @Test
    void testUsuarioNaoElegivelPedagio50_PassouMaisDe2Anos() {
        // ✅ Criando um usuário masculino que contribuiu antes da reforma, mas faltavam 3 anos para se aposentar
        Usuario usuario = new Usuario("João", "05/05/1970", Usuario.Genero.MASCULINO, Usuario.Profissao.GERAL, 63);

        // ✅ Criando contribuições fictícias (ele começou a trabalhar em 1990 e contribuiu até 2019)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(2, usuario.getId(), 4500.00, LocalDate.of(1990, 1, 1), LocalDate.of(2019, 11, 12))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio50(usuario, contribuicoes);
        assertFalse(elegivel, "Usuário não deveria estar elegível, pois faltavam mais de 24 meses para se aposentar.");
    }

    @Test
    void testUsuarioNaoElegivelPedagio50_SoContribuiuDepoisDe2019() {
        // ✅ Criando um usuário feminino que começou a contribuir depois da reforma
        Usuario usuario = new Usuario("Ana", "20/05/1985", Usuario.Genero.FEMININO, Usuario.Profissao.GERAL, 62);

        // ✅ Criando contribuições fictícias (ela começou a trabalhar só em 2020)
        List<Contribuicao> contribuicoes = Arrays.asList(
                new Contribuicao(3, usuario.getId(), 5000.00, LocalDate.of(2020, 1, 1), LocalDate.of(2025, 12, 31))
        );

        boolean elegivel = elegibilidadeService.isElegivelPedagio50(usuario, contribuicoes);
        assertFalse(elegivel, "Usuário não deveria estar elegível para o Pedágio 50%");
    }
}

