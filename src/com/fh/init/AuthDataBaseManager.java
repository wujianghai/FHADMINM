package com.fh.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class AuthDataBaseManager {

	static JdbcTemplate jdbcTemplate;
	
     public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		AuthDataBaseManager.jdbcTemplate = jdbcTemplate;
	}

	public static void  initTable(String scanningPackage) throws DocumentException, IOException
     {
    	/* InputStream file=	AuthDataBaseManager.class.getResourceAsStream ("create_table.xml");
    	 SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		 Document document = sax.read(file);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 Element root = document.getRootElement();// 获取根节点
		 @SuppressWarnings("unchecked")
		List<Element> listElement = root.elements();// 所有一级子节点的list
		 
		 for (final Element node: listElement) {// 遍历所有一级子节点
			  String name = node.attributeValue("id");// 
			  String value = node.getText();//  
			  jdbcTemplate.execute(value);
		}
		 */
		 try {
			initResourceData( scanningPackage);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
     }
     
	@SuppressWarnings("unchecked")
	private static void  initResourceData(String scanningPackage) throws DocumentException, IOException, ClassNotFoundException
     {
    	 InputStream file=	AuthDataBaseManager.class.getResourceAsStream ("initTable.xml");
    	 SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		 Document document = sax.read(file);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 Element root = document.getRootElement();// 获取根节点
		 List<Element> listElement = root.elements();// 所有一级子节点的list
		 
		 Map<String, String> initSQL=new HashMap<String, String>();
			for (final Element node: listElement) {// 遍历所有一级子节点
				  String name = node.attributeValue("id");// 
				  String value = node.getText();//  
				  initSQL.put(name, value);
			}
		 
			ClasspathPackageScanner scan = new ClasspathPackageScanner(scanningPackage);
			List<String> classList = scan.getFullyQualifiedClassNameList();	
			classList.addAll(scan.getFullyQualifiedClassNameList() );
			
			jdbcTemplate.update("update authority_resource set scan_status=-1 ");
		 
			for(int i=0;i<classList.size();i++)
			{
				
				String className = classList.get(i);
				@SuppressWarnings("rawtypes")
				Class classes = Class.forName(className);
				List<Map<String, Object>> list =  null;		
				List<AuthPassport> authorityList = AnnotationUtil.parseMethodAnnotation(classes);
				for(int j=0;j<authorityList.size();j++)
				{
					AuthPassport authority = authorityList.get(j);
					list = jdbcTemplate.queryForList("select id from  authority_resource where packageName=? and className=? and  methodName=? and ParameterTypes=?",
							authority.getPackageName(),authority.getClassName(),authority.getMethodName(),authority.getParameterTypes());
					if(list.size()>0)
					{
						jdbcTemplate.update(initSQL.get("authResourceUpdate").toString(),
								authority.getName(),authority.getDescription(),authority.getPackageName(),authority.getClassName(),authority.getRequestMapping(),authority.getMethodName(),authority.getParameterTypes(),list.get(0).get("id"));
					}else
					{
						jdbcTemplate.update(initSQL.get("authResourceInsert").toString(),
								authority.getName(),authority.getDescription(),authority.getPackageName(),authority.getClassName(),authority.getRequestMapping(),authority.getMethodName(),authority.getParameterTypes());
					}
				}
				
			}
     }
     
}
