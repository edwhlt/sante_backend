package fr.hedwin;

import fr.hedwin.objects.Profil;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.exceptions.DaoException;
import fr.hedwin.sql.tables.ProfilDaoImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "viewProfilServlet", value = "/vprofil")
public class ProfilServlet extends HttpServlet {

    private ProfilDaoImpl profilDao;

    public void init() throws ServletException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        this.profilDao = daoFactory.getProfilDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        request.getRequestDispatcher("/profil.jsp").forward(request, response);
    }

}
