package Obligatorio1;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // GET /tareas
    public void obtenerTodas(Context ctx) {
        ctx.json(tareaService.obtenerTodas());
        ctx.status(HttpStatus.OK);
    }

    // GET /tareas/{id}
    public void obtenerPorId(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        tareaService.obtenerPorId(id)
                .ifPresentOrElse(
                        tarea -> {
                            ctx.json(tarea);
                            ctx.status(HttpStatus.OK);
                        },
                        () -> ctx.status(HttpStatus.NOT_FOUND)
                );
    }

    // POST /tareas
    public void crear(Context ctx) {
        try {
            System.out.println("POST /tareas body = " + ctx.body());

            Map<String, Object> body = ctx.bodyAsClass(Map.class);

            System.out.println("POST /tareas body parsed = " + body);

            if (body == null) {
                ctx.status(HttpStatus.BAD_REQUEST);
                ctx.json(Map.of("error", "Body JSON no válido"));
                return;
            }

            Object tituloObj = body.get("titulo");
            Object descripcionObj = body.get("descripcion");

            if (tituloObj == null || tituloObj.toString().trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST);
                ctx.json(Map.of("error", "El titulo es obligatorio"));
                return;
            }

            String titulo = tituloObj.toString();
            String descripcion = descripcionObj != null ? descripcionObj.toString() : "";

            Tarea nueva = tareaService.crear(titulo, descripcion);
            ctx.json(nueva);
            ctx.status(HttpStatus.CREATED);

        } catch (Exception e) {
            System.out.println("ERROR en POST /tareas:");
            e.printStackTrace();

            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(Map.of(
                    "error", "Error interno del servidor",
                    "mensaje", e.getMessage()
            ));
        }
    }

    // PUT /tareas/{id}
    public void actualizar(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        String titulo = body.get("titulo") != null ? body.get("titulo").toString() : null;
        String descripcion = body.get("descripcion") != null ? body.get("descripcion").toString() : null;

        if (titulo != null && titulo.trim().isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("error", "El titulo no puede estar vacío"));
            return;
        }

        tareaService.actualizar(id, titulo, descripcion, null)
                .ifPresentOrElse(
                        tarea -> {
                            ctx.json(tarea);
                            ctx.status(HttpStatus.OK);
                        },
                        () -> ctx.status(HttpStatus.NOT_FOUND)
                );
    }

    // DELETE /tareas/{id}
    public void eliminar(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        boolean eliminada = tareaService.eliminar(id);
        if (eliminada) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    // PATCH /tareas/{id}/completar
    public void marcarCompletada(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        tareaService.marcarCompletada(id)
                .ifPresentOrElse(
                        tarea -> {
                            ctx.json(tarea);
                            ctx.status(HttpStatus.OK);
                        },
                        () -> ctx.status(HttpStatus.NOT_FOUND)
                );
    }
}
