package com.example.tpfinaltusi.DAO;

import android.util.Log;

import com.example.tpfinaltusi.db.PostgreSQLConnection;
import com.example.tpfinaltusi.entidades.CodigoQR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CodigoQRDAO {
    private Connection connection;
    private CountDownLatch connectionLatch = new CountDownLatch(1);

    public CodigoQRDAO() {
        conectarBaseDeDatos();
    }

    private void conectarBaseDeDatos() {
        PostgreSQLConnection.connectToDatabase(new PostgreSQLConnection.DatabaseConnectionCallback() {
            @Override
            public void onConnectionSuccess(Connection connection) {
                CodigoQRDAO.this.connection = connection;
                connectionLatch.countDown();
            }

            @Override
            public void onConnectionFailure(String error) {
                Log.e("CodigoQRDAO", error);
                connectionLatch.countDown();
            }
        });
    }

    private void esperarConexion() {
        try {
            connectionLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean crearCodigoQR(CodigoQR codigoQR) {
        esperarConexion();
        String sql = "INSERT INTO codigosqr (codigo, imagen, puntos, usuarioquelocreo,canjeado,idusuario) VALUES (?, ?, ?, ?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigoQR.getCodigo());
            statement.setString(2, codigoQR.getImagen());
            statement.setInt(3, codigoQR.getPuntos());
            statement.setInt(4, codigoQR.getUsuarioQueLoCreo());
            statement.setBoolean(5, codigoQR.isCanjeado());
            statement.setInt(6, codigoQR.getIdusuario());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarCodigoQR(CodigoQR codigoQR) {
        esperarConexion();
        String sql = "UPDATE codigosqr SET codigo=?, imagen=?, puntos=?, usuarioquelocreo=?, canjeado=?, idusuario=? WHERE idcodigoqr=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigoQR.getCodigo());
            statement.setString(2, codigoQR.getImagen());
            statement.setInt(3, codigoQR.getPuntos());
            statement.setInt(4, codigoQR.getUsuarioQueLoCreo());
            statement.setBoolean(5, codigoQR.isCanjeado());
            statement.setInt(5, codigoQR.getIdusuario());
            statement.setInt(7, codigoQR.getIdCodigoQR());
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borrarCodigoQR(int idCodigoQR) {
        esperarConexion();
        String sql = "DELETE FROM codigosqr WHERE idcodigoqr=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCodigoQR);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public CodigoQR traerCodigoQRPorId(int idCodigoQR) {
        esperarConexion();
        String sql = "SELECT * FROM codigosqr WHERE idcodigoqr=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCodigoQR);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return crearCodigoQRDesdeResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CodigoQR> traerTodosLosCodigoQRs() {
        esperarConexion();
        List<CodigoQR> codigoQRs = new ArrayList<>();
        String sql = "SELECT * FROM codigosqr";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                codigoQRs.add(crearCodigoQRDesdeResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return codigoQRs;
    }

    private CodigoQR crearCodigoQRDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idCodigoQR = resultSet.getInt("idcodigoqr");
        String codigo = resultSet.getString("codigo");
        String imagen = resultSet.getString("imagen");
        int puntos = resultSet.getInt("puntos");
        int usuarioquelocreo = resultSet.getInt("usuarioquelocreo");
        Boolean canjeado = resultSet.getBoolean("canjeado");
        int idusuario = resultSet.getInt("idusuario");
        return new CodigoQR(idCodigoQR, codigo, imagen, puntos,canjeado,usuarioquelocreo, idusuario);
    }
    public boolean editarEstadoCanjeado(String codigo, boolean nuevoEstado, int idUsuario) {
        esperarConexion();
        String sql = "UPDATE codigosqr SET canjeado=?, idusuario=? WHERE codigo=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, nuevoEstado);
            statement.setInt(2, idUsuario);
            statement.setString(3, codigo);
            int filasAfectadas = statement.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
