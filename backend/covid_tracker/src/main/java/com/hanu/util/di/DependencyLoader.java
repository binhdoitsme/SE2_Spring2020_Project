package com.hanu.util.di;

import javassist.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hanu.util.configuration.Configuration;

public class DependencyLoader {  
    // constants
    private static final String ROOT_PACKAGE = Configuration.get("io.rootpackage");
    private static final Class<? extends Annotation> INJECT_ANNOTATION_CLASS = Inject.class;
    private static final Class<?> DEPENDENCY_CONTAINER_CLASS = DependencyContainer.class;

    private static ClassPool classPool = ClassPool.getDefault();

    public static void loadDependencies() throws IOException, ClassNotFoundException, CannotCompileException, NotFoundException {

        // scan every java file in this project
        List<CtClass> classList = ClassPathClassLoader.getClasses(ROOT_PACKAGE).stream()
                .map(c -> getCtClassFromClassName(c))
                .collect(Collectors.toList());

        // find fields, constructors, setters with @Inject annotations
        List<CtField> injectedFields = getInjectedFieldsFromClassList(classList, INJECT_ANNOTATION_CLASS);
        Map<CtClass, List<CtField>> map = createClassFieldMap(injectedFields);
        for (CtClass clazz : map.keySet()) {
            setFieldDependency(clazz, map.get(clazz));
        }
    }

    private static Map<CtClass, List<CtField>> createClassFieldMap(List<CtField> injectFields) {
        Map<CtClass, List<CtField>> map = new HashMap<>();
        for (CtField field : injectFields) {
            CtClass clazz = field.getDeclaringClass();
            if (map.get(clazz) == null) {
                map.put(clazz, new LinkedList<>());
            }
            map.get(clazz).add(field);
        }
        return map;
    }

    private static void setFieldDependency(CtClass clazz, List<CtField> injectedFields)
            throws NotFoundException, CannotCompileException, ClassNotFoundException {
        CtConstructor defaultConstructor = clazz.getDeclaredConstructor(null);
        for (CtField injectedField : injectedFields) {
            defaultConstructor.insertAfter(getDependencySetterStatement(injectedField));
        }
        clazz.toClass();
    }

    private static String getDependencySetterStatement(CtField field) throws NotFoundException {
        CtClass fieldType = field.getType();
        return field.getName()
                + " = "
                + "(" + fieldType.getName() + ")"
                + DEPENDENCY_CONTAINER_CLASS.getCanonicalName() + ".locateDependency(" + fieldType.getName() + ".class);";
    }

    private static List<CtField> getInjectedFieldsFromClassList(List<CtClass> classList,
                                                                Class<? extends Annotation> annotationType) {
        if (classList.isEmpty()) {
            return new ArrayList<>();
        }
        return classList.stream()
                .map(c -> new ArrayList<>(Arrays.asList(c.getDeclaredFields())).stream()
                        .filter(f -> f.hasAnnotation(annotationType))
                        .collect(Collectors.toList()))
                .reduce((a, b) -> {
                    a.addAll(b);
                    return a;
                }).get();
    }

    private static CtClass getCtClassFromClassName(String qualifiedClassName) {
        try {
            return classPool.getCtClass(qualifiedClassName);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
