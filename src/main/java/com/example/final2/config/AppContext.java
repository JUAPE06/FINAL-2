package com.example.final2.config;

import com.example.final2.controller.*;
import com.example.final2.repository.*;
import com.example.final2.service.*;

/**
 * Contenedor de Inyección de Dependencias (DI).
 * Instancia y conecta todas las capas: config → repository → service → controller.
 */
public class AppContext {

    private static AppContext instance;

    private final DatabaseConfig dbConfig;
    private final IDocenteRepository docenteRepository;
    private final IDocenteService docenteService;
    private final IDeudaRepository deudaRepository;
    private final IDeudaService deudaService;

    private AppContext() {
        this.dbConfig               = new DatabaseConfig();
        this.docenteRepository      = new DocenteRepository(dbConfig);
        this.docenteService         = new DocenteService(docenteRepository);
        this.deudaRepository        = new DeudaRepository(dbConfig);
        this.deudaService           = new DeudaService(deudaRepository);
    }

    public static AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    /**
     * Factory de controladores para FXMLLoader.setControllerFactory().
     * Inyecta el servicio en cada controlador.
     */
    public Object getController(Class<?> type) {
        if (type == DeudaController.class) {
            return new DeudaController(deudaService);
        }
        if (type == DeudaFormController.class) {
            return new DeudaFormController(deudaService, docenteService);
        }
        if (type == DocenteController.class) {
            return new DocenteController(docenteService);
        }
        if (type == DocenteFormController.class) {
            return new DocenteFormController(docenteService);
        }
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public void destroy() {
        dbConfig.close();
    }
}
