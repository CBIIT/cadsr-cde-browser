/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.model;

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
public class CsCsiValueMeaningModelList {
	private Set<String> participatingAttributes;//These are designations or definitions
	private List<CsCsiValueMeaningModel> modelList;

	public CsCsiValueMeaningModelList(List<CsCsiValueMeaningModel> models) {
		participatingAttributes = new HashSet<String>();
		if (models != null) {
			for (CsCsiValueMeaningModel model : models) {
				participatingAttributes.add(model.getAttIdseq());
			}
			this.modelList = models;
		}
	}
	
	public List<CsCsiValueMeaningModel> getModels() {
		return modelList;
	}

	/**
	 * This function is to check if a particular AC is referred in this classification list.
	 * 
	 * @param idSeq
	 * @return true if idseq is refereed in this classifications list
	 */
	public boolean isAttClassified(String idSeq) {
		return participatingAttributes.contains(idSeq);
	}
	public Set<String> getParticipatingAttributes() {
		return participatingAttributes;
	}

}
