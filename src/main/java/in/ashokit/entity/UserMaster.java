package in.ashokit.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "USER_MASTER")
public class UserMaster {
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "MOBILE")
	private Long mobile;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DOB")
	private LocalDate dob;
	
	@Column(name = "SSN")
	private Long ssn;
	
	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ACC_STATUS")
	private String accStatus;

	@Column(name = "CREATED_DATE",updatable=false)
	@CreationTimestamp
	private LocalDate createdDate;

	@Column(name = "UPDATED_DATE",insertable=false)
	@UpdateTimestamp
	private LocalDate updatedDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

}
