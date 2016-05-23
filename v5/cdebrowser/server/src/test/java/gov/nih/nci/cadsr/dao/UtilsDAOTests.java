/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.lang.reflect.Method;

import gov.nih.nci.cadsr.dao.model.SearchModel;
/**
 * 
 * @author asafievan
 *
 */
public class UtilsDAOTests {
	/**
	 * Auxilary method to build expected class field list of PermissibleValuesModel class
	 * 
	 * @param targetClass
	 */
	public static void findModelAttributes(Class<?> targetClass) {
    	Method[] methodTargetArr = targetClass.getMethods();
    	
    	for (int i = 0; i < methodTargetArr.length; i++) {
    		Method method = methodTargetArr[i];
    		String methodName = method.getName();
    		if (methodName.startsWith("set")) {
    			//find similar method in source class
    			System.out.print('"' + methodName.substring(3) + "\", ");
    		}
    	}
    	System.out.println("\nDone");
    }
	public static void findModelAttributesDb(Class<?> targetClass) {
    	Method[] methodTargetArr = targetClass.getMethods();
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < methodTargetArr.length; i++) {
    		Method method = methodTargetArr[i];
    		String methodName = method.getName();
    		if (methodName.startsWith("set")) {
    			//find similar method in source class
    			char[] nameStr = methodName.substring(3).toCharArray();
    			
    			sb.append('"').append(Character.toLowerCase(nameStr[0]));
    			if (nameStr.length > 0) {
    			for (int j = 1; j < nameStr.length; j++) {
    				char currChar= nameStr[j];
    				if (Character.isUpperCase(currChar)) {
    					sb.append('_');
    				}
    				sb.append(Character.toLowerCase(nameStr[j]));
    			}
    			sb.append("\", ");
    			}
    		}
    	}
    	System.out.println(sb.toString() + "\nDone");
    }
	public static void main(String[] args) {
		findModelAttributesDb(SearchModel.class);
	}
}
