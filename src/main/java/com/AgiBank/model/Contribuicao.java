package com.AgiBank.model;

import java.time.LocalDate;

public class Contribuicao {

    private int idContribuicao;
    private int idUsuario;
    private double valorSalario;
    private LocalDate periodoInicio;
    private LocalDate periodoFim;

    public Contribuicao(int idUsuario, double valorSalario, LocalDate periodoInicio, LocalDate periodoFim) {
        this.idUsuario = idUsuario;
        this.valorSalario = valorSalario;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
    }

    //Getters e Setter

    //Id contribuição
    public int getIdContribuicao() {
        return idContribuicao;
    }

    public void setIdContribuicao(int idContribuicao) {
        this.idContribuicao = idContribuicao;
    }

    //Id Usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Valor salario
    public double getValorSalario() {
        return valorSalario;
    }

    public void setValorSalario(double valorSalario) {
        this.valorSalario = valorSalario;
    }

    //Periodo fim
    public LocalDate getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDate periodoFim) {
        this.periodoFim = periodoFim;
    }

    //Periodo Inicio
    public LocalDate getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDate periodoInicio) {
        this.periodoInicio = periodoInicio;
    }
}
