package Obligatorio1;

import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        TareaService tareaService = new TareaService();
        TareaController controller = new TareaController(tareaService);

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(7070);

        // Endpoints
        app.get("/tareas", controller::obtenerTodas);
        app.get("/tareas/{id}", controller::obtenerPorId);
        app.post("/tareas", controller::crear);
        app.put("/tareas/{id}", controller::actualizar);
        app.delete("/tareas/{id}", controller::eliminar);
        app.patch("/tareas/{id}/completar", controller::marcarCompletada);
    }
}
