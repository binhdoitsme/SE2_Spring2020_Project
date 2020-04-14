package com.hanu.util.newscrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.hanu.domain.dto.Article;
import com.hanu.domain.dto.Link;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The class handling crawling CoViD-19 news over the net
 * @layer framework & driver
 * @author BinhDH
 */
public class Newscrawler {

    // constants
    private static final byte COUNT_PER_SOURCE = 6;
    private static final Map<String, String> SOURCES = new HashMap<String, String>() {
        private static final long serialVersionUID = -792587553483629961L;
        {
            put("vtv", "https://vtv.vn/dong-su-kien/dai-dich-covid-19-242.htm");
            put("vnexpress", "https://vnexpress.net/tag/covid-19-1266216");
        }
    };
    private static final Map<String, NewsFormat[]> FORMATS = new HashMap<String, NewsFormat[]>() {
        private static final long serialVersionUID = -8049175202147902011L;
        {
            put("vtv",
                    new NewsFormat[] { new NewsFormat("div.focus1", "h2 > a", "a > img"),
                            new NewsFormat("div.focus2", "h2 > a", "a > img"),
                            new NewsFormat("li.tlitem", "h4 > a", "a > img") });
            put("vnexpress",
                    new NewsFormat[] { new NewsFormat("article", "h3 > a", "div > a > picture > img") });
        }
    };
    private static final Map<String, String> SITE_ROOTS = new HashMap<String, String>() {
        private static final long serialVersionUID = -3053507219034151605L;
        {
            put("vtv", "https://vtv.vn");
            put("vnexpress", "https://vnexpress.net");
        }
    };

    public Newscrawler() { }

    public List<Article> getArticles() throws IOException {
        List<Article> articles = new ArrayList<>();
        for (String provider : SOURCES.keySet()) {
            articles.addAll(getArticlesByProvider(provider));
        }
        return articles;
    }

    private List<Article> getArticlesByProvider(String provider) throws IOException {
        String link = SOURCES.get(provider);
        String content = getContentFromUrl(link, "GET");
        Document doc = Jsoup.parse(content);
        NewsFormat[] newsFormats = FORMATS.get(provider);
        String siteRoot = SITE_ROOTS.get(provider);        
        return getArticlesFromProviderFormats(newsFormats, doc, provider, siteRoot);
    }

    private List<Article> getArticlesFromProviderFormats(NewsFormat[] newsFormats, Document doc, 
                                                            String provider, String siteRoot) {
        List<Article> articles = new ArrayList<>();
        for (NewsFormat nf : newsFormats) {
            Elements elements = doc.select(nf.getArticleSelector());
            for (Element e : elements) {
                if (articles.size() < COUNT_PER_SOURCE) {
                    Article a = createArticleFrom(e, nf, provider, siteRoot);
                    if (a != null) articles.add(a);
                } else {
                    break;
                }
            }
        }
        return articles;
    }

    private String getContentFromUrl(String link, String method) throws IOException {
        URL url = new URL(link);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream stream = connection.getInputStream();
        return readFromStream(stream);
    }

    private String readFromStream(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder content = new StringBuilder();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            content.append(currentLine).append("\n");
        }
        stream.close();
        return content.toString();
    }

    private Article createArticleFrom(Element e, NewsFormat nf, String provider, String siteRoot) {
        try {
            String headerSelector = nf.getHeaderSelector();
            String imgSelector = nf.getImgSelector();
            String heading = e.selectFirst(headerSelector).text();
            String absUrl = siteRoot.concat(e.selectFirst(headerSelector).attributes().get("href").replace(siteRoot, ""));
            Element imgElement = e.selectFirst(imgSelector);
            String imgUrl = imgElement.attributes().get("src");
            imgUrl = imgUrl.startsWith("data:") ? imgElement.attributes().get("data-src") : imgUrl;
            return new Article(provider, 
                                new Link(absUrl, "article", "GET"),
                                heading,
                                new Link(imgUrl, "thumb", "GET"));
        } catch (Exception ex) {
            return null;
        }
    }
}