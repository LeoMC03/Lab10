package com.example.lab10_grupodinamin.Daos;

import com.example.lab10_grupodinamin.Beans.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

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

    public String obtenerStartDate(int idjob, int id_employee) {
        String endDate= new String();

        String sql="select end_date\n" +
                "from job_history\n" +
                "where employee_id like ? and job_id like ?;";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, id_employee);
            pstmt.setInt(2, idjob);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    endDate=rs.getString(1);

                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return endDate;
    }


    public void agregarJobHistory (int employee_id,String start_date, int job_id,int department_id) {


        String sql="insert into job_history ( employee_id, start_date,end_date,job_id,department_id)\n" +
                "values (?,?,now(),?,?);";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, employee_id);
            pstmt.setString(2, start_date);
            pstmt.setInt(3, job_id);
            pstmt.setInt(4, department_id);



        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
