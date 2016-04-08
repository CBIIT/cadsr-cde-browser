package gov.nih.nci.cadsr.error;

public class RestControllerException extends Exception
{
	private static final long serialVersionUID = -7907653634832661523L;
	
	public RestControllerException()
	{
		super();
	}

	public RestControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestControllerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public RestControllerException(String message)
	{
		super(message);
	}

	public RestControllerException(Throwable cause)
	{
		super(cause);
	}

}
