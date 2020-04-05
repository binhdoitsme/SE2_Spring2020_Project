package com.hanu.util.server;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServletLoader {

    private static final String PACKAGE_SEPARATOR = ".";
    private static final String PATH_SEPARATOR = "/";
    private static final String PARENT_DIR = "./src/main/java/";
    private static final Logger logger = LoggerFactory.getLogger(ServletLoader.class);
    ;

    private static String packageToPath(String packageName) {
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    private static String fullyQualifiedClassNameFrom(String packageName, String className) {
        return packageName.concat(".").concat(className.replace(".java", ""));
    }

    @SuppressWarnings("unchecked")
    private static void addServletClassToCollection(List<Class<HttpServlet>> servletClasses,
                                                    Class<?> servletClass) {
        if (!servletClass.equals(HttpServlet.class) && !servletClass.getSuperclass().equals(HttpServlet.class)) {
            return;
        }
        if (servletClass.getAnnotation(WebServlet.class) == null) {
            return;
        }
        servletClasses.add((Class<HttpServlet>) servletClass);
        logger.info("Added servlet class: " + servletClass.getCanonicalName());
    }

    public static List<Class<HttpServlet>> getServletClasses(String servletPackageName)
            throws ClassNotFoundException {
        logger.info("Initializing servlet classes...");
        String servletPkgPath = PARENT_DIR.concat(packageToPath(servletPackageName));
        String[] servletClassFileNames = new File(servletPkgPath).list();
        List<Class<HttpServlet>> servletClasses = new ArrayList<>();
        assert servletClassFileNames != null;
        for (String servletClassName : servletClassFileNames) {
            Class<?> servletClass = Class.forName(fullyQualifiedClassNameFrom(servletPackageName, servletClassName));
            addServletClassToCollection(servletClasses, servletClass);
        }
        logger.info("Finished loading servlets.");
        return servletClasses;
    }
}
