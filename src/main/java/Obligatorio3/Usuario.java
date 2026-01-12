package Obligatorio3;

import java.time.LocalDateTime;

public class Usuario {
    private String username;
    private String password;
    private String email;
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(String username, String password, String email, LocalDateTime fechaRegistro) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
