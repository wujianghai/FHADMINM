package com.fh.init;

public class AuthPassport {

	private Long ID;
	private String name;
	private  String requestMapping; 
	
	private  String packageName;
	private  String className;
	private String methodName;
	private String ParameterTypes;
	private String returnType;
	private String description;
	private int authType=0;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRequestMapping() {
		return requestMapping;
	}
	public void setRequestMapping(String requestMapping) {
		this.requestMapping = requestMapping;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getParameterTypes() {
		return ParameterTypes;
	}
	public void setParameterTypes(String parameterTypes) {
		ParameterTypes = parameterTypes;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public int getAuthType() {
		return authType;
	}
	public void setAuthType(int authType) {
		this.authType = authType;
	}
	  
	
	
	
}
