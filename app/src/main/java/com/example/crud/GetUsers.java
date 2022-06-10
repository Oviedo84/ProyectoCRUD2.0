package com.example.crud;

public class GetUsers {
    private String usuario_id, nivel_id, password, usuario;

    public GetUsers() { }

    public GetUsers(String usuario_id, String nivel_id, String password, String usuario) {
        this.usuario_id = usuario_id;
        this.nivel_id = nivel_id;
        this.password = password;
        this.usuario = usuario;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public String getNivel_id() {
        return nivel_id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsuario() {
        return usuario;
    }


}
