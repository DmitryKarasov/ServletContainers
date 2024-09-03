package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.util.ServletUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {
    private final static String URI = "/api/posts";
    private final static String GET = "GET";
    private final static String POST = "POST";
    private final static String DELETE = "DELETE";
    private PostController controller;

    @Override
    public void init() {
        var context = new AnnotationConfigApplicationContext("ru.netology");
        controller = context.getBean(PostController.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var method = req.getMethod();

            if (req.getRequestURI().startsWith(URI)) {
                switch (method) {
                    case GET:
                        doGet(req, resp);
                        break;
                    case POST:
                        doPost(req, resp);
                        break;
                    case DELETE:
                        doDelete(req, resp);
                        break;
                    default:
                        resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        if (path.equals(URI)) {
            controller.all(resp);
        } else if (path.matches(URI + "/\\d+")) {
            controller.getById(ServletUtil.parseId(path), resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        controller.save(req.getReader(), resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getRequestURI();
        if (path.matches(URI + "/\\d+")) {
            controller.removeById(ServletUtil.parseId(path), resp);
        }
    }
}

