package com.AgiBank;

import com.AgiBank.controller.ContribuicaoController;
import com.AgiBank.controller.UsuarioController;
import com.AgiBank.dao.contribuicao.ContribuicaoDAOImpl;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.model.Contribuicao;
import com.AgiBank.model.Usuario;
import com.AgiBank.view.contribuicao.ContribuicaoView;
import com.AgiBank.view.resultadoAposentadoria.SimuladorView;
import com.AgiBank.view.usuario.UsuarioView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        // Inicialização dos DAOs
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        ContribuicaoDAOImpl contribuicaoDAO = new ContribuicaoDAOImpl();

        // Criação da view e controller para o usuário
        UsuarioView usuarioView = new UsuarioView();
        UsuarioController usuarioController = new UsuarioController(usuarioDAO, usuarioView);
        usuarioController.criarUsuario();

        // Obtendo o ID do usuário criado
        int idUsuario = usuarioController.getIdUsuarioAtual();

        // Criação da view e controller para contribuições
        ContribuicaoController contribuicaoController = new ContribuicaoController(contribuicaoDAO);
        ContribuicaoView contribuicaoView = new ContribuicaoView(contribuicaoController);

        // Iniciar o processo de cadastro das contribuições para o usuário
        contribuicaoView.iniciarCadastroContribuicoes(idUsuario);

        // 🔹 Recuperar as contribuições cadastradas
        List<Contribuicao> contribuicoes = contribuicaoDAO.buscarPorUsuario(idUsuario);

        // 🔹 Inicializar a SimuladorView e exibir a simulação
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);

        if (usuario != null && !contribuicoes.isEmpty()) {
            SimuladorView simuladorView = new SimuladorView();
            simuladorView.exibirSimulacao(usuario, contribuicoes);
        } else {
            System.out.println("⚠️ Erro: Usuário ou contribuições não encontrados. Simulação não realizada.");
        }
    }
}
