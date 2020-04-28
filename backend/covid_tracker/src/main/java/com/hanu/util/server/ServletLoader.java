package com.hanu.util.server;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ServletLoader {

    private static final String PACKAGE_SEPARATOR = ".";
    private static final String PATH_SEPARATOR = "/";
    private static final String PARENT_DIR = "./src/main/java/";
    private static final Logger logger = LoggerFactory.getLogger(ServletLoader.class);;

    private static String packageToPath(String packageName) {
        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }

    private static String fullyQualifiedClassNameFrom(String packageName, String className) {
        return packageName.concat(".").concat(className.replace(".java", "")).replace(".class", "");
    }

    @SuppressWarnings("unchecked")
    private static void addServletClassToCollection(List<Class<HttpServlet>> servletClasses, Class<?> servletClass) {
        if (!servletClass.equals(HttpServlet.class) && !servletClass.getSuperclass().equals(HttpServlet.class)) {
            return;
        }
        if (servletClass.getAnnotation(WebServlet.class) == null) {
            return;
        }
        servletClasses.add((Class<HttpServlet>) servletClass);
        logger.info("Added servlet class: " + servletClass.getCanonicalName());
    }

    private static String[] getServletClassFileNamesFromJar(String servletPackageName) throws IOException {
        String currentJarFile = ServletLoader.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath()
                                    .substring(1);

        JarFile file = new JarFile(currentJarFile);
        Enumeration<JarEntry> files = file.entries();
        List<String> servletClasses = new ArrayList<>();
        String pkgPath = packageToPath(servletPackageName) + "/";
        while (files.hasMoreElements()) {
            String name = files.nextElement().getName();
            if (name.startsWith(pkgPath) && !name.equals(pkgPath)) {
                servletClasses.add(name.replace(pkgPath, ""));
            }
        }
        file.close();
        return servletClasses.toArray(new String[servletClasses.size()]);
    }

    public static List<Class<HttpServlet>> getServletClasses(String servletPackageName) throws ClassNotFoundException,
            IOException {
        logger.info("Initializing servlet classes...");
        String servletPkgPath = PARENT_DIR.concat(packageToPath(servletPackageName));

        String[] servletClassFileNames = null;

        try {
            servletClassFileNames = new File(servletPkgPath).list();
            @SuppressWarnings("unused") Object o = servletClassFileNames[0];
        } catch (Exception e) {
            servletClassFileNames = getServletClassFileNamesFromJar(servletPackageName);
        }

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
