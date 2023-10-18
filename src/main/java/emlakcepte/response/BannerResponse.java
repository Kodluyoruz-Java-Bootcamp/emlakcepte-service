package emlakcepte.response;

import org.springframework.http.HttpStatus;

public class BannerResponse {

	private HttpStatus httpStatus;
	private String ilanNo;
	private int adet;
	private String telNo1;
	private String telNo2;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getIlanNo() {
		return ilanNo;
	}

	public void setIlanNo(String ilanNo) {
		this.ilanNo = ilanNo;
	}

	public int getAdet() {
		return adet;
	}

	public void setAdet(int adet) {
		this.adet = adet;
	}

	public String getTelNo1() {
		return telNo1;
	}

	public void setTelNo1(String telNo1) {
		this.telNo1 = telNo1;
	}

	public String getTelNo2() {
		return telNo2;
	}

	public void setTelNo2(String telNo2) {
		this.telNo2 = telNo2;
	}

}
