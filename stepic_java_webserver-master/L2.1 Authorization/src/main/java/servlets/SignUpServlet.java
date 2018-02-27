package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author v.chibrikov
 * <p>
 * Пример кода для курса на https://stepic.org/
 * <p>
 * Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign in
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        if (accountService.getUserByLogin(login) != null) {
            response.getWriter().println(login + " reg");
        } else {
            response.getWriter().println(login + " unreg");
            accountService.addNewUser(new UserProfile(login, password, email));
        }


//        if (login == null || password == null) {
//            response.setContentType("text/html;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        UserProfile profile = accountService.getUserByLogin(login);
//        if (profile == null || !profile.getPass().equals(password)) {
//            response.setContentType("text/html;charset=utf-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        accountService.addNewUser(new UserProfile("login", "password","email"));
//        accountService.addSession(request.getSession().getId(), profile);
//        System.out.println(accountService);
//        Gson gson = new Gson();
//        String json = gson.toJson(profile.getLogin());
//        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("Registered: " + login + " " + password + " " + email);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    //sign out
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            accountService.deleteSession(sessionId);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("Goodbye!");
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
