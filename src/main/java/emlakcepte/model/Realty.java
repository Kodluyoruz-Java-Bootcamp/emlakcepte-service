package emlakcepte.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import emlakcepte.model.enums.RealtyType;

@Entity
@Table(name = "realty")
public class Realty {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	@Column(name = "relaty_no", nullable = false)
	private Integer no;
	@Column(name = "title", nullable = false, length = 200)
	private String title;
	@Column(name = "create_date")
	private LocalDateTime createDate;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private RealtyType status;
	@Column(name = "province")
	private String province;

	public Realty() {
		super();
	}

	public Realty(Integer no, String title, LocalDateTime createDate, RealtyType status) {
		super();
		this.no = no;
		this.title = title;
		this.createDate = createDate;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RealtyType getStatus() {
		return status;
	}

	public void setStatus(RealtyType status) {
		this.status = status;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "Realty [no=" + no + ", title=" + title + ", createDate=" + createDate + ", status=" + getStatus()
				+ ", province=" + province + "]";
	}

}
