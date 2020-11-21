package com.mimorphism.mangotracko.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor 
@Table(name= "mangoes")
@Getter
@Setter
public class Mango {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="mangoId")
    private Long mangoId;

    @Column(name="mangoTitle")
    @NotEmpty(message="* Enter Mango Title")
    private String mangoTitle;
    
    @Column(name="author")
    private String author; 
    
    @Column(name="totalChapter")
    private int totalChapter;
    
    @Column(name="lastReadChapter")
    private int lastReadChapter;
    
    @Column(name="lastReadTime")
    private String lastReadTime;
    
    @Column(name="startReadTime")
    private String startReadTime;    
    
    @Column(name="remarks")
    private String remarks;
    
        
	public Mango(@NotEmpty(message = "* Enter Mango Title") String mangoTitle, String author, int totalChapter,
			int lastReadChapter, String lastReadTime, String startReadTime, String remarks) {
		super();
		this.mangoTitle = mangoTitle;
		this.author = author;
		this.totalChapter = totalChapter;
		this.lastReadChapter = lastReadChapter;
		this.lastReadTime = lastReadTime;
		this.startReadTime = startReadTime;
		this.remarks = remarks;
	}


}

