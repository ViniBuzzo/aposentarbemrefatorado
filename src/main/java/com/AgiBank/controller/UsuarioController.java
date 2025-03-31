package com.AgiBank.controller;


import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.model.Usuario;
import com.AgiBank.view.usuario.UsuarioView;

public class UsuarioController {
    private final UsuarioDAOImpl usuarioDAO;
    private final UsuarioView usuarioView;
    private int idUsuarioAtual;


    public UsuarioController(UsuarioDAOImpl usuarioDAO, UsuarioView usuarioView) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioView = usuarioView;
    }

    public void criarUsuario() {
        Usuario usuario = usuarioView.coletarDadosUsuario(); // Cria o usuário através da view
        boolean sucesso = usuarioDAO.criarUsuario(usuario);

        if (sucesso) {
            idUsuarioAtual = usuario.getId();
            usuarioView.exibirMensagem("Usuário cadastrado com sucesso!\n");
        } else {
            usuarioView.exibirMensagem("Erro ao cadastrar usuário.");
        }
    }

    public int getIdUsuarioAtual() {
        return idUsuarioAtual;
    }
}
