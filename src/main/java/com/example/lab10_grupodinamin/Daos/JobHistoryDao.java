package com.example.lab10_grupodinamin.Daos;

import com.example.lab10_grupodinamin.Beans.*;

import java.sql.*;
import java.util.ArrayList;

public class JobHistoryDao extends DaoBase{

    public ArrayList<JobHistory> listar(int idjob) {
        JobHistory jobHistory = null;
        ArrayList<JobHistory> lista = new ArrayList<>();
        System.out.println(idjob);
        String sql="SELECT jh.employee_id,jh.start_date,jh.end_date,j.job_title,d.department_name FROM job_history jh\n" +
                "inner join departments d on jh.department_id=d.department_id\n" +
                "inner join jobs j on jh.job_id=j.job_id\n" +
                "where employee_id=?;";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idjob);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobHistory = new JobHistory();
                    Job job = new Job();
                    Department department = new Department();
                    jobHistory.setIdE(rs.getInt(1));
                    jobHistory.setFechaS(rs.getString(2));
                    jobHistory.setFechaE(rs.getString(3));
                    job.setJobTitle(rs.getString(4));
                    jobHistory.setJob(job);
                    department.setDepartmentName(rs.getString(5));
                    jobHistory.setDepartment(department);
                    lista.add(jobHistory);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

}
