package gov.nih.nci.cadsr.service.search;

public interface ProcessConstants  {
  //Related to the dataelement resource/page
  static final String DE_IDSEQ = "deidseq";
  static final String DE_PREFERRED_NAME = "deprefname";
  static final String DE_LONG_NAME = "delongname";
  static final String DE_PREFERRED_DEFINITION = "deprefdef";
  static final String DE_VD_IDSEQ = "devdidseq";
  static final String DE_ASL_NAME = "deaslname";
  static final String DE_LATEST_VERSION_IND = "delatestind";
  static final String DELETED_IND = "dedelind";
  static final String DE_CREATED_BY = "decreatedby";
  static final String DE_DATE_CREATED = "dedatecreated";
  static final String DE_MODIFIED_BY = "demodifiedby";
  static final String DE_DATE_MODIFIED = "demodifieddate";
  static final String ALL_DATA_ELEMENTS = "dataelementsvector";


  //Related to the valid values
  static final String VALID_VALUES_VECTOR = "validvaluesvector";
  static final String VALID_VALUES_LIST = "validvalueslist";
  static final String VALID_VALUES_PAGE_NUMBER = "vvPageNumber";
  static final String VALID_VALUES_PAGE_SCROLLER = "vvPageScroller";
  static final String VALID_VALUES_PAGE_LIST = "validvaluespagelist";
  static final String VD_IDSEQ = "vdidseq";
  static final String VD_SHORT_MEANING = "vdshortmeaning";
  static final String VD_SHORT_MEANING_DESC = "vdshortmeaningdescription";
  static final String VD_SHORT_MEANING_VALUE = "vdshortmeaningvalue";
  static final String VD_SHORT_MEANING_CONCEPT = "vdshortmeaningconcept";

  //Related to the Classification Schemes
  static final String CS_DE_IDSEQ = "csdeidseq";
  static final String CLASSIFICATION_VECTOR = "classificationsvector";
  static final String CS_NAME = "csschemename";
  static final String CS_DEFINITION = "csdefinition";
  static final String CS_ITEM_NAME = "csitemname";
  static final String CS_ITEM_TYPE = "csitemtype";

  // Related to Value Domains
  static final String VALUEDOMAIN = "vd";

  //Related to templates
  static final String TEMPLATE_IDSEQ = "templateIdseq";
  static final String DOCUMNET_IDSEQ = "documentIdseq";
  static final String REFERENCE_BLOB_VO = "rbVO";

  //Related to XML Downloads
  static final String XML_DOCUMENT = "xmlString";

  //Related to LOV's
  static final String VD_LOV = "vdlb";
  static final String DEC_LOV = "declb";
  static final String CS_LOV = "cslb";
  static final String PROTO_LOV = "protolb";

  //Related to Page Context
  static final String PAGE_CONTEXT = "pgContext";

  //Related to Page Iterators
  static final String DE_SEARCH_PAGE_ITERATOR = "deSearchPageIterator";
  static final String DE_SEARCH_RESULTS_PAGE_NUMBER = "deSearchPageNum";
  static final String DE_SEARCH_PAGE_SCROLLER = "deSearchPageScroller";
  static final String DE_SEARCH_TOP_PAGE_SCROLLER = "deSearchTopPageScroller";
  static final String DE_SEARCH_QUERY_BUILDER = "deSearchQueryBuilder";
  static final String TREE_TRANSFER_OBJECT = "treeTO";
  static final String DE_CS_PAGE_ITERATOR = "deCsPageIterator";
  static final String DE_CS_RESULTS_PAGE_NUMBER = "pageNum";
  static final String DE_CS_PAGE_SCROLLER = "deCsPageScroller";
  static final String DE_CS_PAGE_SCROLLER_HTML = "deCsPageScrollerHTML";
  static final String TOP_PAGE_SCROLLER = "top";
  static final String BOTTOM_PAGE_SCROLLER = "bottom";
  static final String DE_FORM_PAGE_ITERATOR = "deFrmPageIterator";
  static final String DE_FORM_RESULTS_PAGE_NUMBER = "deFrmPageNum";
  static final String DE_FORM_PAGE_SCROLLER = "deFrmPageScroller";
  static final String DE_FORM_PAGE_SCROLLER_HTML = "deFrmPageScrollerHTML";

  static final String DE_USAGES_LIST = "deUsagesList";
  static final String SELECT_DE = "selectDE";
  public static final String CDE_CART_ADD_SUCCESS = "cdecart.add.success";
  public static final String CDE_SEARCH_RESULT_COMPARATOR="cdeSearchResultComparator";
  
  public static final String  FORMS_IGNORE_FILTER = "forms.ignore.filter";
  
  public static final String DE_SEARCH_MODE_EXACT = "Exact phrase";
  public static final String DE_SEARCH_MODE_ALL = "All of the words";
  public static final String DE_SEARCH_MODE_ANY = "At least one of the words";
  
  public static final String VD_TYPE_ENUMERATED = "enumerated";
  public static final String VD_TYPE_NON_ENUMERATED = "nonenumerated";
  public static final String VD_TYPE_BOTH = "both";
  
  public static final String DOWNLOAD_LINK_WIKI = "DOWNLOAD_LINK_WIKI";
}
