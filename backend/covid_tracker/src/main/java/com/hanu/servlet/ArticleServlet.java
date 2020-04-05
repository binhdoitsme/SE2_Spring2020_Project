package com.hanu.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanu.domain.dto.Article;
import com.hanu.util.newscrawler.Newscrawler;

@WebServlet(name="articles", urlPatterns = "/articles")
public class ArticleServlet extends HttpServlet {
    
    private static final long serialVersionUID = -2474270407387725094L;

    private void writeAsJson(HttpServletResponse resp, Object content)
            throws UnsupportedEncodingException, IOException {
        // write json to response
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        Writer out = new BufferedWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
        out.write(new ObjectMapper().writeValueAsString(content));
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Newscrawler crawler = new Newscrawler();
        List<Article> articles = crawler.getArticles();
        writeAsJson(resp, articles);
    }
}