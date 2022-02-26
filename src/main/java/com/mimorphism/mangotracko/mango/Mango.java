package com.mimorphism.mangotracko.mango;


import javax.persistence.*;


import org.springframework.lang.Nullable;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

