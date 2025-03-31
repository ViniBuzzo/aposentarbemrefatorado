package com.AgiBank.service;

import com.AgiBank.model.Contribuicao;
import java.time.LocalDate;
import java.util.List;

public class ElegibilidadeService {

    private static final LocalDate DATA_REFORMA = LocalDate.of(2019, 11, 13);

    public boolean contribuiuAntesDaReforma(List<Contribuicao> contribuicoes) {
        return contribuicoes.stream()
                .anyMatch(c -> c.getPeriodoInicio().isBefore(DATA_REFORMA) || c.getPeriodoFim().isBefore(DATA_REFORMA));
    }}

//    public boolean seAposentouAntesDaReforma(List<Contribuicao> contribuicoes){
//
//    }
//}
