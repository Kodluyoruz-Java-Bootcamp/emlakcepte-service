package emlakcepte.exception;

public class EmlakCepteException extends RuntimeException {

	private String key;

	public EmlakCepteException(String key) {
		super(key);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
