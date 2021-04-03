package com.mimorphism.mangotracko.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity 
@Table(name= "mt_backlog")
@Getter
@Setter
public class OngoingMango 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="backlog_seq")
	@SequenceGenerator(name = "backlog_seq")
	@Column(name="BACKLOG_ID")
	private Long backlogID;
	
	@Column(name="MANGO_TITLE")
	private String mangoTitle;
	
    @Column(name="LAST_CHAPTER")
	private int lastChapterRead;
	
    @Column(name="LAST_READ_TIME")
	private String lastReadTime;
	
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MANGO_ID", nullable = false)
    @JsonBackReference
    private Mango mango;
	

}
