package com.game.qs.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * Created by zun.wei on 2019/5/3.
 * 文件工具类
 */
public class FileTools implements Serializable {

    /**
     * 加载 .properties 属性配置文件
     *
     * @param pathName 配置文件所在目录
     * @param fileName 文件名称包括后缀名称，如：jdbc.properties
     * @return map，如：key => mysql.username , value => root
     */
    public static Map<String, String> loadProperties(String pathName, String fileName) {
        IOFileFilter fileFilter = FileFilterUtils.nameFileFilter(fileName);
        return FileTools.loadProperties(pathName, fileFilter);
    }

    /**
     * 加载 .properties 属性配置文件
     *
     * @param pathName       配置文件所在目录
     * @param fileNamePrefix 配置文件名的前缀
     * @param fileNameSuffix 配置文件名的后缀 包含 .properties
     * @return map，如：key => mysql.username , value => root
     */
    public static Map<String, String> loadProperties(String pathName, String fileNamePrefix, String fileNameSuffix) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter(fileNamePrefix)
                , FileFilterUtils.suffixFileFilter(fileNameSuffix));
        return FileTools.loadProperties(pathName, fileFilter);
    }

    public static Map<String, String> loadPropertiesByFileSuffix(String pathName, String fileNameSuffix) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter("")
                , FileFilterUtils.suffixFileFilter(fileNameSuffix));
        return FileTools.loadProperties(pathName, fileFilter);
    }

    public static Map<String, String> loadPropertiesByFilePrefix(String pathName, String fileNamePrefix) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter(fileNamePrefix)
                , FileFilterUtils.suffixFileFilter(""));
        return FileTools.loadProperties(pathName, fileFilter);
    }

    /**
     * 加载 .properties 属性配置文件
     *
     * @param pathName   配置文件所在目录
     * @param fileFilter IOFileFilter
     * @return map，如：key => mysql.username , value => root
     */
    public static Map<String, String> loadProperties(String pathName, IOFileFilter fileFilter) {
        Collection<File> listFiles = FileUtils.listFiles(new File(pathName), fileFilter, null);
        Map<String, String> result = new HashMap<>();
        listFiles.parallelStream().forEach(file -> {
            FileReader fileReader = null;
            try {
                Properties prop = new Properties();
                System.out.println("prop = " + prop);
                fileReader = new FileReader(file);
                prop.load(fileReader);
                prop.stringPropertyNames().parallelStream()
                        .forEach(e -> result.put(e, prop.getProperty(e)));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (Objects.nonNull(fileReader)) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return result;
    }

    /**
     * 加载 .yml 属性配置文件
     *
     * @param pathName    配置文件所在目录
     * @param fileName    文件名称包括后缀名称，如：jdbc.yml
     * @param resultClass 返回值类型
     * @param <T>         返回值类型
     * @return 返回值类型
     */
    public static <T> Optional<T> loadYaml(String pathName, String fileName, Class<T> resultClass) {
        IOFileFilter fileFilter = FileFilterUtils.nameFileFilter(fileName);
        return loadYaml(pathName, resultClass, fileFilter);
    }

    /**
     * 加载 .yml 属性配置文件
     *
     * @param pathName       配置文件所在目录
     * @param fileNamePrefix 配置文件名的前缀
     * @param fileNameSuffix 配置文件名的后缀 包含 .properties
     * @param resultClass    返回值类型
     * @param <T>            返回值类型
     * @return 返回值类型
     */
    public static <T> Optional<T> loadYaml(String pathName, String fileNamePrefix, String fileNameSuffix, Class<T> resultClass) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter(fileNamePrefix)
                , FileFilterUtils.suffixFileFilter(fileNameSuffix));
        return loadYaml(pathName, resultClass, fileFilter);
    }

    public static <T> Optional<T> loadYamlByFilePrefix(String pathName, String fileNamePrefix, Class<T> resultClass) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter(fileNamePrefix)
                , FileFilterUtils.suffixFileFilter(""));
        return loadYaml(pathName, resultClass, fileFilter);
    }

    public static <T> Optional<T> loadYamlByFileSuffix(String pathName, String fileNameSuffix, Class<T> resultClass) {
        IOFileFilter fileFilter = FileFilterUtils.and(
                FileFilterUtils.prefixFileFilter("")
                , FileFilterUtils.suffixFileFilter(fileNameSuffix));
        return loadYaml(pathName, resultClass, fileFilter);
    }


    /**
     * 加载 .yml 属性配置文件
     *
     * @param resultClass 返回值类型
     * @param fileFilter  IOFileFilter
     * @param <T>         返回值类型
     * @return 返回值类型
     */
    public static <T> Optional<T> loadYaml(String pathName, Class<T> resultClass, IOFileFilter fileFilter) {
        Collection<File> listFiles = FileUtils.listFiles(new File(pathName), fileFilter, null);
        if (listFiles.isEmpty())
            return Optional.empty();

        Yaml yaml = new Yaml();
        File file = new ArrayList<>(listFiles).get(0);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            System.out.println("file = " + file);
            return Optional.ofNullable(yaml.loadAs(fileInputStream, resultClass));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (Objects.nonNull(fileInputStream)) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 判断文件是否存在
    public static void initFile(File file) {
        if (file.exists()) {
            System.out.println("file exists");
        } else {
            System.out.println("file not exists, create it ...");
            try {
                boolean b = file.createNewFile();
                System.out.println("createNewFile b --::"+ b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 判断文件夹是否存在
    public static void initDir(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                System.out.println("dir exists");
            } else {
                System.out.println("the same name file exists, can not create dir");
            }
        } else {
            System.out.println("dir not exists, create it ...");
            boolean b = file.mkdirs();
            System.out.println("mkdirs b --::" + b);
        }
    }

}
