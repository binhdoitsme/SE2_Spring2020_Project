package com.hanu.util.di;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassPathClassLoader {
    private static boolean isLoadingFromJar() {
        String currentJarFile = ClassPathClassLoader.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath()
                                    .substring(1);
        return currentJarFile.endsWith(".jar");
    }

    private static List<String> loadClassesFromJar(String packageName) throws IOException {
        List<String> classNames = new ArrayList<>();
        String currentJarFile = ClassPathClassLoader.class.getProtectionDomain()
                                    .getCodeSource().getLocation().getPath()
                                    .substring(1);
        JarFile file = new JarFile(currentJarFile);
        Enumeration<JarEntry> files = file.entries();

        String pkgPath = packageName.replace(".", "/") + "/";
        while (files.hasMoreElements()) {
            String name = files.nextElement().getName();
            if (name.startsWith(pkgPath) && !name.equals(pkgPath) && name.endsWith(".class")) {
                classNames.add(name.replace(".class", "").replace("/", "."));
            }
        }
        file.close();
        return classNames;
    }

    public static List<String> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        if (isLoadingFromJar()) return loadClassesFromJar(packageName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<String> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<String> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<String> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
            }
        }
        return classes;
    }
}
