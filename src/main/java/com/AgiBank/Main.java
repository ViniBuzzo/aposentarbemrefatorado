package com.AgiBank;

import com.AgiBank.controller.UsuarioController;
import com.AgiBank.dao.usuario.UsuarioDAOImpl;
import com.AgiBank.view.usuario.UsuarioView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Inicialização dos DAOs
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

        // Criação da view do usuário
        UsuarioView usuarioView = new UsuarioView();
        UsuarioController usuarioController = new UsuarioController(usuarioDAO, usuarioView);
        usuarioController.criarUsuario();
    }
}