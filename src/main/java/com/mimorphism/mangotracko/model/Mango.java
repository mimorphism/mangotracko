package com.mimorphism.mangotracko.model;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor 
@Table(name= "mt_mangoes")
@Getter
@Setter
public class Mango {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)  
	@Column(name="MANGO_ID")
    private Long mangoId;

    @Column(name="MANGO_TITLE")
    private String mangoTitle;
    
    @Column(name="STATUS")
    private String mangoStatus;
    
    
    @Column(name="COMPLETION_DATE")
    private String completionDateTime;    
    
    @Column(name="REMARKS")
    private String remarks;
    
    @Column(name="IMG_PATH")
    private String img;
    
    @Column(name="AUTHOR")
    private String author;
    
    @Nullable
    @OneToOne(fetch = FetchType.EAGER,
            cascade =  CascadeType.ALL,
            mappedBy = "mango")
    @JsonManagedReference
    private OngoingMango ongoingMango;
    
    



}

