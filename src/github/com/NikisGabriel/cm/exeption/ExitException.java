package github.com.NikisGabriel.cm.exeption;

public class ExitException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ExitException() {
		super();
	}

	public ExitException(String message) {
		super(message);
	}
}
