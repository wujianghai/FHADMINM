package com.fh.init;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

/*2016-03-07*/
public class AnnotationUtil {
	
	/*获取方法的权限
	 * 
	 * */
	public static  List<AuthPassport> parseMethodAnnotation(@SuppressWarnings("rawtypes") Class obj)
	{
		
		List<AuthPassport> list=new ArrayList<AuthPassport>();
		String requestMappingClass = "";
		String packageName = obj.getPackage().getName();
		String className = obj.getSimpleName();
		
		//类信息
		@SuppressWarnings("unchecked")
		Annotation classAnno= obj.getAnnotation(RequestMapping.class);
		if (classAnno != null)
			{
			 RequestMapping requestMapping =(RequestMapping) classAnno;
			 requestMappingClass=(requestMapping.value()[0]);
			}
		
		//方法
		Method[] methodList = obj.getMethods();
		 for(int i=0;i<methodList.length;i++)
		 {
			 Method method =methodList[i]; 
//			 com.fh.init.annon.AuthPassport fireAuthority =method.getAnnotation(com.fh.init.annon.AuthPassport.class);
			 
//			  if(fireAuthority!=null)
//				 {
				  AuthPassport authority = new AuthPassport();
					
					//设置方法相关信息
					Annotation annotationinfo=method.getAnnotation(  com.fh.init.annon.AuthPassport.class);
//					if (annotationinfo != null)
//							{
						com.fh.init.annon.AuthPassport requestMapping =(com.fh.init.annon.AuthPassport) annotationinfo;
						String name="";
						String description="";
						if(requestMapping!=null){
							if( !(requestMapping.value()==null || requestMapping.value().equals("")) )
								name+=requestMapping.value();
							if( !(requestMapping.name()==null || requestMapping.name().equals("")) )
								name+=requestMapping.name();
							if( !(requestMapping.desc()==null || requestMapping.desc().equals("")) )
								description+=requestMapping.desc();
						}
						authority.setName(name);
						authority.setDescription(description);
//							}
					
				 annotationinfo=method.getAnnotation(RequestMapping.class);
					if (annotationinfo != null)
							{
							 RequestMapping requestMapping1 =(RequestMapping) annotationinfo;
							 authority.setRequestMapping(requestMappingClass+(requestMapping1.value().length!=0?requestMapping1.value()[0]:""));
							}
						authority.setPackageName(packageName);
						authority.setClassName(className);
						authority.setMethodName(method.getName());
						@SuppressWarnings("rawtypes")
						Class[] param=method.getParameterTypes();
						String parametersName="";
						for(int j=0;j<param.length;j++)
						{
							parametersName+=" | "+( param[j].getName() ); 
						}
						parametersName=parametersName.replaceFirst(" | ", "");
						authority.setParameterTypes(  parametersName);

					list.add(authority);
//				 }
		 }
		return list;
	}

}
