package com.fh.init;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ResourceUtils;

public class CoreConfiguration implements ApplicationContextAware {

   private  String	configPath;
   private JdbcTemplate jdbcTemplate;
   public   ApplicationContext context = null;   

public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
}

public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	this.jdbcTemplate = jdbcTemplate;
}

public String getConfigPath() {
	return configPath;
}

public void setConfigPath(String configPath) {
	this.configPath = configPath;
}

 private void  init() throws FileNotFoundException
 {
	 File configFile = ResourceUtils.getFile(configPath);
	 InputStream in = new BufferedInputStream (new FileInputStream(configFile));
	 
	 Properties prop = new Properties();     
	 try {
		prop.load(in);
		try {
			loadData(prop);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}  catch (SecurityException e) {
			e.printStackTrace();
		}
		 in.close();
	} catch (IOException e) {
		e.printStackTrace();
	}  
 }
 
 private void  loadData(Properties prop) throws InstantiationException, IllegalAccessException, ClassNotFoundException, DocumentException, IOException, SecurityException
 {
	 AuthDataBaseManager.setJdbcTemplate(jdbcTemplate);
	 AuthDataBaseManager.initTable(prop.getProperty("auth.AuthPassportScanning"));
 }

@Override
public void setApplicationContext(ApplicationContext context)
		throws BeansException {
	this.context=context;
	Runnable run=new Runnable(){

		@Override
		public void run() {
			try {
				init();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	};
	Thread thread = new Thread(run) ;
	thread.start();
	
}
  
  
}
