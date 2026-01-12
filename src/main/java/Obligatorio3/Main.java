package Obligatorio3;

import io.javalin.Javalin;
public class Main {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(7070);


        app.post("/auth/registrar", AuthController::registrar);
        app.post("/auth/login", AuthController::login);


        app.before("/perfil", AuthController::verificarAutenticacion);
        app.get("/perfil", AuthController::obtenerPerfil);
    }
}
