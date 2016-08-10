/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * This auxilary class purpose is to quickly check if a particular definition or designation is referred by the given classification list.
 * This class holds a set of participating unique IDs which is build only once for the list.
 * 
 * @author asafievan
 *
 */
public class CsCsiDeModelList {
	private Set<String> csCsiIdSet = new HashSet<>();;//These are designations or definitions
	private List<CsCsiDeModel> modelList;
	public CsCsiDeModelList(List<CsCsiDeModel> modelList) {
		super();
		if (modelList != null) {
			this.modelList = modelList;
			for (CsCsiDeModel csCsiDeModel : modelList) {
				if (csCsiDeModel != null) {
					csCsiIdSet.add(csCsiDeModel.getCsCsiIdseq());
				}
			}
		}
		else {
			this.modelList = new ArrayList<>();
		}
	}
	public List<CsCsiDeModel> getModelList() {
		return modelList;
	}
	public Set<String> getCsCsiIdSet() {
		return csCsiIdSet;
	}
	/**
	 * This is a auxiliary method to return SQL list of IDs.
	 * @return a String as " ('A', 'B') " for SQL generation.
	 */
	public String buildInSql() {
		if (csCsiIdSet.isEmpty())
			return " NULL ";
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		for (String str : csCsiIdSet) {
			sb.append("'").append(str).append("', ");
		}
		String res = sb.toString();
		res = res.substring(0, res.length() - 2);
		res += (") ");
		return res;
	}
	@Override
	public String toString() {
		return "CsCsiDeModelList [csCsiIdSet=" + csCsiIdSet + ", modelList=" + modelList + "]";
	}
}
