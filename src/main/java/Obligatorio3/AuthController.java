package Obligatorio3;

import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuthController {

    public static void registrar(Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String username = (String) body.get("username");
        String password = (String) body.get("password");
        String email = (String) body.get("email");

        if (username == null || password == null || email == null) {
            ctx.status(400).json(Map.of("error", "username, password y email son obligatorios"));
            return;
        }

        if (AuthStore.usuarios.containsKey(username)) {
            ctx.status(409).json(Map.of("error", "El username ya está registrado"));
            return;
        }

        Usuario usuario = new Usuario(username, password, email, LocalDateTime.now());
        AuthStore.usuarios.put(username, usuario);

        ctx.status(201).json(Map.of(
                "mensaje", "Usuario registrado exitosamente",
                "username", username
        ));
    }

    public static void login(Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String username = (String) body.get("username");
        String password = (String) body.get("password");

        if (username == null || password == null) {
            ctx.status(400).json(Map.of("error", "username y password son obligatorios"));
            return;
        }

        Usuario usuario = AuthStore.usuarios.get(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            throw new UnauthorizedResponse("Credenciales inválidas");
        }

        String token = username + "_" + System.currentTimeMillis();
        AuthStore.tokens.put(token, username);

        ctx.status(200).json(Map.of(
                "token", token,
                "username", username
        ));
    }

    public static void obtenerPerfil(Context ctx) {
        String token = ctx.header("Authorization");
        String username = validarToken(token);

        if (username == null) {
            throw new UnauthorizedResponse("Token inválido");
        }

        Usuario usuario = AuthStore.usuarios.get(username);

        Map<String, Object> perfil = new HashMap<>();
        perfil.put("username", usuario.getUsername());
        perfil.put("email", usuario.getEmail());
        perfil.put("fechaRegistro", usuario.getFechaRegistro().toString());

        ctx.status(200).json(perfil);
    }

    public static String validarToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        return AuthStore.tokens.get(token);
    }

    public static void verificarAutenticacion(Context ctx) {
        String token = ctx.header("Authorization");
        if (token == null || token.isBlank()) {
            throw new UnauthorizedResponse("No autorizado. Token requerido");
        }

        String username = validarToken(token);
        if (username == null) {
            throw new ForbiddenResponse("Token inválido o expirado");
        }

        ctx.attribute("username", username);
    }
}
