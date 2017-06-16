/**
 * Copyright (C) 2017 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * This class encapsulate operations on retrieving Designations and Definitions for Detail Views.
 * @author asafievan
 *
 */
public abstract class BaseDesignationDefinitionModel extends BaseModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Hashmap of DesignationModels indexed by designationIdseq
     */
	protected HashMap<String, DesignationModel> designationModels; // from DesignationsView.ac_idseq = data_elements.de_idseq
    /**
     * Hashmap of DefinitionModels indexed by definitionIdseq
     */
	protected HashMap<String, DefinitionModel> definitionModels;
    /**
     * Hashmap of CsCsiModels indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     */
	protected HashMap<String, CsCsiModel> csCsiData;
    /**
     * Hashmap of Lists of designationIdseqs indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     * (each entry is a list of the designationIdseqs that are classified into the indexed csiIdseq)
     */
	protected HashMap<String, List<String>> csCsiDesignations;
    /**
     * Hashmap of Lists of definitionIdseqs indexed by csiIdseq. csiIdseq may be the value "UNCLASSIFIED"
     * (each entry is a list of the definitionIdseqs that are classified into the indexed csiIdseq)
     */
	protected HashMap<String, List<String>> csCsiDefinitions;

    public void setDesignationModels( List<DesignationModel> designationModels )
    {
        setDesignationModels( new HashMap<String, DesignationModel>() );
        for( DesignationModel designationModel : designationModels )
        {
            getDesignationModels().put( designationModel.getDesigIDSeq(), designationModel );
        }
    }

    public void setDefinitionModels( List<DefinitionModel> definitionModels )
    {
        setDefinitionModels( new HashMap<String, DefinitionModel>() );
        for( DefinitionModel definitionModel : definitionModels )
        {
            getDefinitionModels().put( definitionModel.getDefinIdseq(), definitionModel );
        }
    }
    
    public void fillCsCsiData( List<CsCsiModel> csCsiModels )
    {
        // initialize csCsiData
        csCsiData = new HashMap<String, CsCsiModel>();
        // Prepare a CsCsiModel for any definitions and designations that are unclassified
        csCsiData.put( CsCsiModel.UNCLASSIFIED, new CsCsiModel( CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED, CsCsiModel.UNCLASSIFIED ) );
        // copy over the rest of the models, using the hashmap to remove duplicates
        for( CsCsiModel csCsiModel : csCsiModels )
        {
            csCsiData.put( csCsiModel.getCsIdseq(), csCsiModel );
        }
    }

    public void fillCsCsiDesignations()
    {
        if( getCsCsiDesignations() == null )
        {
            setCsCsiDesignations( new HashMap<String, List<String>>() );
        }
        for( DesignationModel designationModel : getDesignationModels().values() )
        {
            if( designationModel.getCsiIdseqs() != null && designationModel.getCsiIdseqs().size() > 0 )
            {
                for( String csiIdseq : designationModel.getCsiIdseqs() )
                {
                    if( getCsCsiDesignations().get( csiIdseq ) == null )
                    {
                        getCsCsiDesignations().put( csiIdseq, new ArrayList<String>() );
                    }
                    getCsCsiDesignations().get( csiIdseq ).add( designationModel.getDesigIDSeq() );
                }
            }
        }

    }

    public void fillCsCsiDefinitions()
    {
        if( getCsCsiDefinitions() == null )
        {
            setCsCsiDefinitions( new HashMap<String, List<String>>() );
        }
        for( DefinitionModel definitionModel : getDefinitionModels().values() )
        {
            if( definitionModel.getCsiIdseqs() != null && definitionModel.getCsiIdseqs().size() > 0 )
            {
                for( String csiIdseq : definitionModel.getCsiIdseqs() )
                {
                    if( getCsCsiDefinitions().get( csiIdseq ) == null )
                    {
                        getCsCsiDefinitions().put( csiIdseq, new ArrayList<String>() );
                    }
                    getCsCsiDefinitions().get( csiIdseq ).add( definitionModel.getDefinIdseq() );
                }
            }
        }

    }

	public HashMap<String, CsCsiModel> getCsCsiData() {
		return csCsiData;
	}

	public void setCsCsiData(HashMap<String, CsCsiModel> csCsiData) {
		this.csCsiData = csCsiData;
	}

	public HashMap<String, List<String>> getCsCsiDesignations() {
		return csCsiDesignations;
	}

	public void setCsCsiDesignations(HashMap<String, List<String>> csCsiDesignations) {
		this.csCsiDesignations = csCsiDesignations;
	}

	public HashMap<String, List<String>> getCsCsiDefinitions() {
		return csCsiDefinitions;
	}

	public void setCsCsiDefinitions(HashMap<String, List<String>> csCsiDefinitions) {
		this.csCsiDefinitions = csCsiDefinitions;
	}

	public HashMap<String, DesignationModel> getDesignationModels() {
		return designationModels;
	}

	public void setDesignationModels(HashMap<String, DesignationModel> designationModels) {
		this.designationModels = designationModels;
	}

	public HashMap<String, DefinitionModel> getDefinitionModels() {
		return definitionModels;
	}

	public void setDefinitionModels(HashMap<String, DefinitionModel> definitionModels) {
		this.definitionModels = definitionModels;
	}
    
}
