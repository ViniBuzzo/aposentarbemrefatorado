package com.AgiBank.service;

import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.AgiBank.model.Usuario.calcularIdade;

public class RegraAposReformaService {

    private static final double PERCENTUAL_BASE = 0.60; // 60%
    private static final double ACRESCIMO_ANUAL = 0.02; // 2% por ano extra
    private static final double TETO_INSS = 8157.41; // Teto do INSS
    private ElegibilidadeService elegibilidadeService;

    public double simularAposentadoria(Usuario usuario, List<Contribuicao> contribuicoes) {
        int idadeAtual = calcularIdade(usuario.getDataNascimento());
        int idadeDesejada = usuario.getIdadeAposentadoriaDesejada();

        if (idadeDesejada <= idadeAtual) {
            throw new IllegalArgumentException("A idade de aposentadoria desejada deve ser maior que a idade atual!");
        }

        // Verificar se contribuiu antes da reforma de 2019
        boolean contribuiuAntesDaReforma = elegibilidadeService.contribuiuAntesDaReforma(contribuicoes);

        // 1️⃣ Calcula o total de meses já contribuídos
        int mesesContribuidos = calcularTotalMesesContribuidos(contribuicoes);
        int anosContribuidos = mesesContribuidos / 12;

        // 2️⃣ Calcula a média das contribuições passadas
        double mediaContribuicoes = calcularMediaContribuicoes(contribuicoes, mesesContribuidos);

        // 3️⃣ Tempo restante até a aposentadoria
        int anosFaltando = idadeDesejada - idadeAtual;

        // 4️⃣ Simulamos a contribuição futura (assumindo que ele continuará contribuindo com a mesma média)
        int mesesFuturos = anosFaltando * 12;
        int anosFuturos = mesesFuturos / 12;
        int totalAnosContribuidos = anosContribuidos + anosFuturos;

        // 5️⃣ Definir tempo mínimo de contribuição com base no gênero e profissão
        int minimoContribuicao;
        int idadeMinimaAposentadoria;

        if (usuario.getProfissao() == Usuario.Profissao.PROFESSOR && !contribuiuAntesDaReforma) {
            // Professores seguem regras especiais se começaram após 2019
            minimoContribuicao = 25;
            idadeMinimaAposentadoria = (usuario.getGenero() == Usuario.Genero.FEMININO) ? 57 : 60;
        } else {
            // Regra padrão
            minimoContribuicao = (usuario.getGenero() == Usuario.Genero.FEMININO) ? 15 : 20;
            idadeMinimaAposentadoria = idadeDesejada;  // Mantemos a idade desejada para a simulação
        }

        // 7️⃣ Calcular anos extras além do mínimo
        int anosExtras = Math.max(0, totalAnosContribuidos - minimoContribuicao);

        // 8️⃣ Calcular coeficiente de aposentadoria (limite máximo 100%)
        double coeficiente = 0.60 + (anosExtras * 0.02);
        coeficiente = Math.min(coeficiente, 1.0);

        // 9️⃣ Calcular valor da aposentadoria
        double valorAposentadoria = mediaContribuicoes * coeficiente;

        // 🔟 Aplica o teto do INSS (se houver)
        return Math.min(valorAposentadoria, TETO_INSS);
    }



    /**
     * Calcula o total de meses de contribuição considerando todas as contribuições do usuário.
     */
    private int calcularTotalMesesContribuidos(List<Contribuicao> contribuicoes) {
        int totalMeses = 0;
        for (Contribuicao c : contribuicoes) {
            totalMeses += ChronoUnit.MONTHS.between(c.getPeriodoInicio(), c.getPeriodoFim());
        }
        return totalMeses;
    }

    /**
     * Calcula a média salarial das contribuições do usuário.
     */
    private double calcularMediaContribuicoes(List<Contribuicao> contribuicoes, int totalMeses) {
        if (totalMeses == 0) return 0.0; // Evita divisão por zero

        double somaSalarios = 0.0;
        for (Contribuicao c : contribuicoes) {
            long mesesContribuidos = ChronoUnit.MONTHS.between(c.getPeriodoInicio(), c.getPeriodoFim());
            somaSalarios += c.getValorSalario() * mesesContribuidos; // Peso por meses
        }

        return somaSalarios / totalMeses;
    }
}
