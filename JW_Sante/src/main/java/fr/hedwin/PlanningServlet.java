package fr.hedwin;

import fr.hedwin.objects.Profil;
import fr.hedwin.objects.aliment.Aliment;
import fr.hedwin.objects.elements.Recette;
import fr.hedwin.objects.elements.Repas;
import fr.hedwin.objects.manage.RecetteMap;
import fr.hedwin.objects.manage.RepasMap;
import fr.hedwin.sql.DATA;
import fr.hedwin.sql.dao.DaoFactory;
import fr.hedwin.sql.dao.DaoInterface;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "planningServlet", value = "/planning-servlet")
public class PlanningServlet extends HttpServlet {

    private DaoInterface<String, Recette> recettesDao;
    private DaoInterface<String, Repas> repasDao;
    private DaoInterface<Integer, Profil> profilDao;

    public void init() throws ServletException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        this.recettesDao = daoFactory.getRecetteDao();
        this.repasDao = daoFactory.getRepasDao();
        this.profilDao = daoFactory.getProfilDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        String str_id = request.getParameter("id");
        try{
            if(str_id == null) str_id = "1";
            int id = Integer.parseInt(str_id);
            Profil profil = DATA.getProfil(id);
            RecetteMap recetteMap = DATA.getRecetteWithNutriment(id);
            RepasMap repasMap = DATA.getRepasWithNutriment(id);

            /*LinkedHashMap<String, DayResult> week = Arrays.stream(DayResult.Day.values())
                    .sorted((e1, e2) -> Comparator.comparing(DayResult.Day::getRang).compare(e1, e2))
                    .map(d -> DATA.getDay(repasMap, 1, "A", d))
                    .collect(Collectors.toMap(e -> e.getWeek()+"-"+e.getDay(), e -> e, (x, y) -> y, LinkedHashMap::new));*/

            Map<String, Aliment> ingredientMap = DATA.memories;

            request.setAttribute("profil", profil);
            request.setAttribute("aliments", ingredientMap);
            request.setAttribute("recettes", recetteMap);
            request.setAttribute("repas", repasMap);
            //request.setAttribute("dayResults", week);
        }catch (Exception ex){
           throw new IOException(ex);
        }
        request.getRequestDispatcher("/planning.jsp").forward(request, response);
    }

    public void destroy() {
    }
}