package gov.nih.nci.cadsr.error;

public class AutheticationFailureException extends Exception
{
	private static final long serialVersionUID = 6528551638773663226L;

	public AutheticationFailureException()
	{
		super();
	}

	public AutheticationFailureException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AutheticationFailureException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AutheticationFailureException(String message)
	{
		super(message);
	}

	public AutheticationFailureException(Throwable cause)
	{
		super(cause);
	}

}
