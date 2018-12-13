package com.mangues.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class DispatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OutputStream outputStream = resp.getOutputStream();
        outputStream.write("Http/1.1 200 OK\r\n".getBytes());
        outputStream.write("Content-Length: 12\r\n\r\n".getBytes());
        outputStream.write("Hello Servlet".getBytes());
        super.doGet(req, resp);
    }
}
