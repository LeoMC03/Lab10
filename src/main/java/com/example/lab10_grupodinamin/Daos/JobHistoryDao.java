package com.example.lab10_grupodinamin.Daos;

import com.example.lab10_grupodinamin.Beans.Country;
import com.example.lab10_grupodinamin.Beans.JobHistory;
import com.example.lab10_grupodinamin.Beans.Location;

import java.sql.*;
import java.util.ArrayList;

public class JobHistoryDao extends DaoBase{

    public ArrayList<JobHistory> listar(int idjob) {
        JobHistory jobHistory = null;
        ArrayList<JobHistory> lista = new ArrayList<>();
        System.out.println(idjob);
        String sql="SELECT * FROM hr.job_history where employee_id=?;";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idjob);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobHistory = new JobHistory();
                    jobHistory.setIdE(rs.getInt(1));
                    jobHistory.setFechaE(rs.getString(2));
                    jobHistory.setFechaS(rs.getString(3));
                    jobHistory.setIdJ(rs.getString(4));
                    jobHistory.setIdD(rs.getInt(5));
                    lista.add(jobHistory);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

}
