package model;

public class AccountGi‡Esistente extends Exception
{
	private static final long serialVersionUID = 1L;

	public AccountGi‡Esistente(String message) 
	{
		super(message);
	}
	
	public AccountGi‡Esistente()
	{
		super("Account gi‡ Esistente");
	}
}
