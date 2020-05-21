package com.fh.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class ClasspathPackageScanner  {
	
	// private Logger logger = LoggerFactory.getLogger(ClasspathPackageScanner.class);
	 
	 
	    private String basePackage;
	    private ClassLoader cl;
	 
	    /**
	     * Construct an instance and specify the base package it should scan.
	     * @param basePackage The base package to scan.
	     */
	    public ClasspathPackageScanner(String basePackage) {
	        this.basePackage = basePackage;
	        this.cl = getClass().getClassLoader();
	 
	    }
	 
	    /**
	     * Construct an instance with base package and class loader.
	     * @param basePackage The base package to scan.
	     * @param cl Use this class load to locate the package.
	     */
	    public ClasspathPackageScanner(String basePackage, ClassLoader cl) {
	        this.basePackage = basePackage;
	        this.cl = cl;
	    }
	 
	    /**
	     * Get all fully qualified names located in the specified package
	     * and its sub-package.
	     *
	     * @return A list of fully qualified names.
	     * @throws IOException
	     */
	   
	    public List<String> getFullyQualifiedClassNameList() throws IOException {
	 
	        return doScan(basePackage, new ArrayList<String>());
	    }
	 
	   
		/**
	     * Actually perform the scanning procedure.
	     *
	     * @param basePackage
	     * @param nameList A list to contain the result.
	     * @return A list of fully qualified names.
	     *
	     * @throws IOException
	     */
	    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
	        // replace dots with splashes
	       
	    	String splashPath = StringUtil.dotToSplash(basePackage);
	 
	    	
	        // get file path
	        URL url = cl.getResource(splashPath);
	        String filePath = StringUtil.getRootPath(url);
	        try {
	        	filePath=URLDecoder.decode(filePath,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	 
	        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
	        if (isJarFile(filePath)) {
	            names = readFromJarFile(filePath, splashPath);
	        } else {
	            names = readFromDirectory(filePath);
	        }
		 if(names!=null){
		        for (String name : names) {
		            if (isClassFile(name)) {
		                //nameList.add(basePackage + "." + StringUtil.trimExtension(name));
		                nameList.add(toFullyQualifiedName(name, basePackage));
		            } else {
		                doScan(basePackage + "." + name, nameList);
		            }
		        }
	        }
	        return nameList;
	    }
	 
	    /**
	     * Convert short class name to fully qualified name.
	     * e.g., String -> java.lang.String
	     */
	    private String toFullyQualifiedName(String shortName, String basePackage) {
	        StringBuilder sb = new StringBuilder(basePackage);
	        sb.append('.');
	        sb.append(StringUtil.trimExtension(shortName));
	 
	        return sb.toString();
	    }
	 
	    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
	        
	 
	        @SuppressWarnings("resource")
			JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
	        JarEntry entry = jarIn.getNextJarEntry();
	 
	        List<String> nameList = new ArrayList<>();
	        while (null != entry) {
	            String name = entry.getName();
	            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
	                nameList.add(name);
	            }
	 
	            entry = jarIn.getNextJarEntry();
	        }
	 
	        return nameList;
	    }
	 
	    private List<String> readFromDirectory(String path) {
	    	/*try {
	    		path=URLDecoder.decode(path,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/
	        File file = new File(path);
	        String[] names = file.list();
	        if (null == names) {
	            return null;
	        }
	        return Arrays.asList(names);
	    }
	 
	    private boolean isClassFile(String name) {
	        return name.endsWith(".class");
	    }
	 
	    private boolean isJarFile(String name) {
	        return name.endsWith(".jar");
	    }
	 
	    /**
	     * For test purpose.
	     */
	    public static void main(String[] args) throws Exception {
	    	ClasspathPackageScanner scan = new ClasspathPackageScanner("cn.fh.lightning.bean");
	        scan.getFullyQualifiedClassNameList();
	    }
	    
	    
	     static class StringUtil {
	        private StringUtil() {
	     
	        }
	     
	        /**
	         * "file:/home/whf/cn/fh" -> "/home/whf/cn/fh"
	         * "jar:file:/home/whf/foo.jar!cn/fh" -> "/home/whf/foo.jar"
	         */
	        public static String getRootPath(URL url) {
	            String fileUrl = url.getFile();
	            int pos = fileUrl.indexOf('!');
	     
	            if (-1 == pos) {
	                return fileUrl;
	            }
	     
	            return fileUrl.substring(5, pos);
	        }
	     
	        /**
	         * "cn.fh.lightning" -> "cn/fh/lightning"
	         * @param name
	         * @return
	         */
	        public static String dotToSplash(String name) {
	            return name.replaceAll("\\.", "/");
	        }
	     
	        /**
	         * "Apple.class" -> "Apple"
	         */
	        public static String trimExtension(String name) {
	            int pos = name.indexOf('.');
	            if (-1 != pos) {
	                return name.substring(0, pos);
	            }
	     
	            return name;
	        }
	     
	        /**
	         * /application/home -> /home
	         * @param uri
	         * @return
	         */
	        public static String trimURI(String uri) {
	            String trimmed = uri.substring(1);
	            int splashIndex = trimmed.indexOf('/');
	            return trimmed.substring(splashIndex);
	        }
	    }
}

