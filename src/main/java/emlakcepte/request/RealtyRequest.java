package emlakcepte.request;

public class RealtyRequest {

	private Integer no;
	private String title;
	private String province;
	private Integer userId;

	public RealtyRequest() {
		super();
	}

	public RealtyRequest(Integer no, String title, String province, Integer userId) {
		super();
		this.no = no;
		this.title = title;
		this.province = province;
		this.userId = userId;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
