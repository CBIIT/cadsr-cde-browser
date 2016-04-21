package gov.nih.nci.cadsr.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig 
{
	private String registrationAuthorityIdentifier;
	
	private String downloadDirectory;
	
	private String fileNamePrefix;
	
	private String cdeDataRestServiceName;
	
	private String maxHoverTextLenStr;

    private String oneContextRestServiceName;

    private String cdesByClassificationSchemeRestServiceName;

    private String cdesByClassificationSchemeItemRestServiceName;

    private String cdesByProtocolRestServiceName;

    private String cdesByProtocolFormRestServiceName;

    private String treeRetrievalWaitMessage;
    
    public String getMaxHoverTextLenStr() {
		return maxHoverTextLenStr;
	}

    @Value( "${maxHoverTextLen}" )
    public void setMaxHoverTextLenStr(String maxHoverTextLenStr) {
		this.maxHoverTextLenStr = maxHoverTextLenStr;
	}

	public String getOneContextRestServiceName() {
		return oneContextRestServiceName;
	}

	@Value( "${oneContextRestService}" )
    public void setOneContextRestServiceName(String oneContextRestServiceName) {
		this.oneContextRestServiceName = oneContextRestServiceName;
	}

	public String getCdesByClassificationSchemeRestServiceName() {
		return cdesByClassificationSchemeRestServiceName;
	}

	@Value( "${cdesByClassificationSchemeRestService}" )
    public void setCdesByClassificationSchemeRestServiceName(String cdesByClassificationSchemeRestServiceName) {
		this.cdesByClassificationSchemeRestServiceName = cdesByClassificationSchemeRestServiceName;
	}

	public String getCdesByClassificationSchemeItemRestServiceName() {
		return cdesByClassificationSchemeItemRestServiceName;
	}

	@Value( "${cdesByClassificationSchemeItemRestService}" )
    public void setCdesByClassificationSchemeItemRestServiceName(String cdesByClassificationSchemeItemRestServiceName) {
		this.cdesByClassificationSchemeItemRestServiceName = cdesByClassificationSchemeItemRestServiceName;
	}

	public String getCdesByProtocolRestServiceName() {
		return cdesByProtocolRestServiceName;
	}

	@Value( "${cdesByProtocolRestService}" )
    public void setCdesByProtocolRestServiceName(String cdesByProtocolRestServiceName) {
		this.cdesByProtocolRestServiceName = cdesByProtocolRestServiceName;
	}

	public String getCdesByProtocolFormRestServiceName() {
		return cdesByProtocolFormRestServiceName;
	}

	@Value( "${cdesByProtocolFormRestService}" )
    public void setCdesByProtocolFormRestServiceName(String cdesByProtocolFormRestServiceName) {
		this.cdesByProtocolFormRestServiceName = cdesByProtocolFormRestServiceName;
	}

	public String getTreeRetrievalWaitMessage() {
		return treeRetrievalWaitMessage;
	}

	@Value( "${treeRetrievalWaitMessage}" )
    public void setTreeRetrievalWaitMessage(String treeRetrievalWaitMessage) {
		this.treeRetrievalWaitMessage = treeRetrievalWaitMessage;
	}

	public String getCdeDataRestServiceName() {
		return cdeDataRestServiceName;
	}

	@Value("${cdeDataRestService}")
    public void setCdeDataRestServiceName(String cdeDataRestServiceName) {
		this.cdeDataRestServiceName = cdeDataRestServiceName;
	}

	public String getRegistrationAuthorityIdentifier() {
		return registrationAuthorityIdentifier;
	}
	
	@Value("${registrationAuthorityIdentifier}")
	public void setRegistrationAuthorityIdentifier(String registrationAuthorityIdentifier) {
		this.registrationAuthorityIdentifier = registrationAuthorityIdentifier;
	}
	
	public String getDownloadDirectory() {
		return downloadDirectory;
	}
	
	@Value("${downloadDirectory}")
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}
	
	public String getFileNamePrefix() {
		return fileNamePrefix;
	}
	
	@Value("${downloadFileNamePrefix}")
	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

}
