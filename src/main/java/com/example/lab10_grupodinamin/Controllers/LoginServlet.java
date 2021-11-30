package com.example.lab10_grupodinamin.Controllers;

import com.example.lab10_grupodinamin.Beans.Employee;
import com.example.lab10_grupodinamin.Daos.EmployeeDao;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "loginform" : request.getParameter("action");
        HttpSession session = request.getSession();

        switch (action) {
            case "loginform":
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession != null && employeeSession.getEmployeeId() > 0) {
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } else {
                    RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                    view.forward(request, response);
                }
                break;
            case "logout":
                session.invalidate();
                response.sendRedirect(request.getContextPath());
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String username = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");

        if (username == null || password == null) {
            request.setAttribute("err", "El usuario o password no pueden ser vacíos");
            RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
            view.forward(request, response);
        }else{
            EmployeeDao employeeDao = new EmployeeDao();
            Employee employee = employeeDao.validarUsuarioPasswordHashed(username, password);

            if (employee != null) {
                HttpSession session = request.getSession();
                session.setAttribute("employeeSession", employee);

                session.setMaxInactiveInterval(10 * 60); // 10 minutos

                Employee emplo = ( Employee) session.getAttribute("employeeSession");
                int id_employee = emplo.getEmployeeId();
                Employee emp = employeeDao.validartop1usuario(id_employee);
                System.out.println(emplo.getJob().getMaxSalary());

                Employee empleado = employeeDao.obtenerEmpleado(id_employee);
                if (emp != null){
                    session.setAttribute("top","- Top 1");
                    response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                } else if (empleado.getJob().getMaxSalary() <= 15000 && emplo.getJob().getMaxSalary() > 8500) {
                    session.setAttribute("top","- Top 2");
                    response.sendRedirect(request.getContextPath() + "/JobServlet");
                }else if(empleado.getJob().getMaxSalary() <= 8500 && emplo.getJob().getMaxSalary() > 5000){
                    session.setAttribute("top","- Top 3");
                    response.sendRedirect(request.getContextPath() + "/DepartmentServlet");
                }else{
                    session.setAttribute("top","- Top 4");
                    response.sendRedirect(request.getContextPath() + "/CountryServlet");
                }

            } else {
                request.setAttribute("err", "El usuario o password no existen");
                RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                view.forward(request, response);
            }
        }

    }
}
