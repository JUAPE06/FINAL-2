package com.example.final2.repository;

import com.example.final2.config.DatabaseConfig;
import com.example.final2.model.Docente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocenteRepository implements IDocenteRepository {

    private final DatabaseConfig dbConfig;

    public DocenteRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Docente> findAll() {
        return findByNombre("");
    }

    @Override
    public List<Docente> findByNombre(String query) {
        String sql = "SELECT * FROM [Docente] WHERE [Nombres] LIKE ? OR [Apellidos] LIKE ? OR [DNI] LIKE ? ORDER BY [Apellidos], [Nombres]";
        List<Docente> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String q = "%" + query.trim() + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ps.setString(3, q);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar docentes", e);
        }
        return list;
    }

    @Override
    public void save(Docente d) {
        String sql = "INSERT INTO [Docente] ([DNI], [Nombres], [Apellidos], [Especialidad], [Condicion]) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, d);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar docente", e);
        }
    }

    @Override
    public void update(Docente d) {
        String sql = "UPDATE [Docente] SET [DNI] = ?, [Nombres] = ?, [Apellidos] = ?, [Especialidad] = ?, [Condicion] = ? WHERE [idDocente] = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, d);
            ps.setInt(6, d.getIdDocente());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar docente", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM [Docente] WHERE [idDocente] = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar docente", e);
        }
    }

    private void fillStatement(PreparedStatement ps, Docente d) throws SQLException {
        ps.setString(1, d.getDni());
        ps.setString(2, d.getNombres());
        ps.setString(3, d.getApellidos());
        ps.setString(4, d.getEspecialidad());
        ps.setString(5, d.getCondicion());
    }

    private Docente mapRow(ResultSet rs) throws SQLException {
        return new Docente(
            rs.getInt("idDocente"),
            rs.getString("DNI"),
            rs.getString("Nombres"),
            rs.getString("Apellidos"),
            rs.getString("Especialidad"),
            rs.getString("Condicion")
        );
    }
}
