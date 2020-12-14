package controllers.likes;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Likes;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikesIndexServlet
 */
@WebServlet("/Likes/Index")
public class LikesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        }catch(Exception e){
            page = 1;
        }

        List<Likes> likes = em.createNamedQuery("getMyAllLikes", Likes.class)
                .setParameter("report", r)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();


        long likes_count = (long)em.createNamedQuery("getMyLikesCount",Long.class)
                .setParameter("report", r)
                .getSingleResult();

        Integer reportUrl = Integer.parseInt(request.getParameter("report_id"));

         em.close();

         request.setAttribute("likes", likes);
         request.setAttribute("likes_count" , likes_count);
         request.setAttribute("page",page);
         request.setAttribute("report_id", r);
         request.setAttribute("reportUrl", reportUrl);



        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/likes/index.jsp");
        rd.forward(request, response);
    }

}
