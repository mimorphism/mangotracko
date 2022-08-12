package com.mimorphism.mangotracko.mango;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mimorphism.mangotracko.appuser.AppUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor 
@NoArgsConstructor
public class Finished implements MangoRecordType {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="FINISHED_ID")
	@NotNull
	private Long finishedId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
	@NotNull
    @JsonBackReference
	private AppUser user;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade=CascadeType.MERGE)
    @JoinColumn(name = "MANGO_ID", nullable = false)
	@NotNull
	@JsonManagedReference
	private Mango mango;
	
	@Column(name="COMPLETION_DATE")
	@NotNull
	private String completionDateTime;    
  
	@Column(name="REMARKS")
	@NotNull
	private String remarks;
	
	

}
