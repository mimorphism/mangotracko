package com.mimorphism.mangotracko.mango;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mimorphism.mangotracko.appuser.AppUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Backlog implements MangoRecordType {

	@Id
	@Column(name="BACKLOG_ID")
	@NotNull
	private Long backlogId;
	
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
	
    @Column(name="ADDED_DATETIME", nullable=false)
	private String addedDateTime;	
}
