package com.AgiBank.service;

import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ElegibilidadeService {

    public static final LocalDate DATA_REFORMA = LocalDate.of(2019, 11, 13);

    public boolean isElegivelPedagio50(Usuario usuario, List<Contribuicao> contribuicoes) {
        // ✅ 1️⃣ Verificar se ele contribuiu antes da reforma
        boolean contribuiuAntes = contribuiuAntesDaReforma(contribuicoes);
        if (!contribuiuAntes) {
            return false; // ❌ Se nunca contribuiu antes da reforma, não pode entrar na regra
        }

        // ✅ 2️⃣ Definir tempo mínimo de contribuição da regra antiga (antes de 2019)
        int minimoContribuicaoAntiga = (usuario.getGenero() == Usuario.Genero.FEMININO) ? 30 * 12 : 35 * 12; // Em meses

        // ✅ 3️⃣ Calcula quantos meses ele já tinha contribuído até 13/11/2019
        int mesesContribuidosAteReforma = calcularMesesContribuidosAteData(contribuicoes, DATA_REFORMA);

        // ✅ 4️⃣ Calcula quanto tempo faltava para atingir o tempo mínimo de contribuição
        int mesesFaltando = minimoContribuicaoAntiga - mesesContribuidosAteReforma;

        // ✅ 5️⃣ Ele é elegível se faltavam **menos de 24 meses** para atingir a regra antiga
        return mesesFaltando > 0 && mesesFaltando <= 24;
    }

    /**
     * Verifica se o usuário contribuiu antes da Reforma da Previdência (13/11/2019).
     */
    public boolean contribuiuAntesDaReforma(List<Contribuicao> contribuicoes) {
        return contribuicoes.stream()
                .anyMatch(c -> c.getPeriodoInicio().isBefore(DATA_REFORMA) || c.getPeriodoFim().isBefore(DATA_REFORMA));
    }

    /**
     * Calcula o total de meses de contribuição do usuário até uma determinada data.
     */
    private int calcularMesesContribuidosAteData(List<Contribuicao> contribuicoes, LocalDate dataLimite) {
        return contribuicoes.stream()
                .filter(c -> c.getPeriodoInicio().isBefore(dataLimite))
                .mapToInt(c -> {
                    LocalDate inicio = c.getPeriodoInicio();
                    LocalDate fim = c.getPeriodoFim().isBefore(dataLimite) ? c.getPeriodoFim() : dataLimite;
                    return (int) ChronoUnit.MONTHS.between(inicio, fim);
                })
                .sum();
    }
}
