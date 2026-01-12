package Obligatorio1;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class TareaService {

    private final Map<Long, Tarea> tareas = new HashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(1);

    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(tareas.values());
    }

    public Optional<Tarea> obtenerPorId(Long id) {
        return Optional.ofNullable(tareas.get(id));
    }

    public Tarea crear(String titulo, String descripcion) {
        Long id = secuenciaId.getAndIncrement();
        Tarea tarea = new Tarea(id, titulo, descripcion);
        tareas.put(id, tarea);
        return tarea;
    }

    public Optional<Tarea> actualizar(Long id, String titulo, String descripcion, Boolean completada) {
        Tarea existente = tareas.get(id);
        if (existente == null) {
            return Optional.empty();
        }
        if (titulo != null) existente.setTitulo(titulo);
        if (descripcion != null) existente.setDescripcion(descripcion);
        if (completada != null) existente.setCompletada(completada);
        return Optional.of(existente);
    }

    public boolean eliminar(Long id) {
        return tareas.remove(id) != null;
    }

    public Optional<Tarea> marcarCompletada(Long id) {
        Tarea existente = tareas.get(id);
        if (existente == null) {
            return Optional.empty();
        }
        existente.setCompletada(true);
        return Optional.of(existente);
    }
}
