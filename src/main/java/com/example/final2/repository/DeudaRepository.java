package com.example.final2.repository;

import com.example.final2.config.DatabaseConfig;
import com.example.final2.model.Deuda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeudaRepository implements IDeudaRepository {

    private final DatabaseConfig dbConfig;

    public DeudaRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public List<Deuda> findAll() {
        return findByFilters("", "", "");
    }

    @Override
    public List<Deuda> findByFilters(String concepto, String docente, String estado) {
        String sql = """
                SELECT d.*, (doc.[Apellidos] + ', ' + doc.[Nombres]) as DocenteNombre
                FROM [Deuda] d
                INNER JOIN [Docente] doc ON d.[idDocente] = doc.[idDocente]
                WHERE d.[Concepto] LIKE ?
                  AND (doc.[Nombres] LIKE ? OR doc.[Apellidos] LIKE ? OR doc.[DNI] LIKE ?)
                  AND d.[Estado] LIKE ?
                ORDER BY d.[FechaRegistro] DESC
                """;
        List<Deuda> list = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String c = "%" + concepto.trim() + "%";
            String doc = "%" + docente.trim() + "%";
            String e = "%" + estado.trim() + "%";

            ps.setString(1, c);
            ps.setString(2, doc);
            ps.setString(3, doc);
            ps.setString(4, doc);
            ps.setString(5, e);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar deudas", e);
        }
        return list;
    }

    @Override
    public void save(Deuda d) {
        String sql = "INSERT INTO [Deuda] ([idDocente], [Concepto], [Monto], [FechaRegistro], [Estado]) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, d);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar deuda", e);
        }
    }

    @Override
    public void update(Deuda d) {
        String sql = "UPDATE [Deuda] SET [idDocente] = ?, [Concepto] = ?, [Monto] = ?, [FechaRegistro] = ?, [Estado] = ? WHERE [idDeuda] = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            fillStatement(ps, d);
            ps.setInt(6, d.getIdDeuda());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar deuda", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM [Deuda] WHERE [idDeuda] = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar deuda", e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        String placeholders = String.join(",", ids.stream().map(i -> "?").toList().toArray(new String[0]));
        String sql = "DELETE FROM [Deuda] WHERE [idDeuda] IN (" + placeholders + ")";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar deudas en bloque", e);
        }
    }

    private void fillStatement(PreparedStatement ps, Deuda d) throws SQLException {
        ps.setInt(1, d.getIdDocente());
        ps.setString(2, d.getConcepto());
        ps.setDouble(3, d.getMonto());
        ps.setDate(4, Date.valueOf(d.getFechaRegistro()));
        ps.setString(5, d.getEstado());
    }

    private Deuda mapRow(ResultSet rs) throws SQLException {
        return new Deuda(
            rs.getInt("idDeuda"),
            rs.getInt("idDocente"),
            rs.getString("Concepto"),
            rs.getDouble("Monto"),
            rs.getDate("FechaRegistro").toLocalDate(),
            rs.getString("Estado"),
            rs.getString("DocenteNombre")
        );
    }
}
