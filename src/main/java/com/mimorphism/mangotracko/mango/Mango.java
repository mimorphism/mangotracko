package com.mimorphism.mangotracko.mango;


import java.util.Optional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
@Table(name= "mangoes")
@Getter
@Setter
public class Mango {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MANGO_ID")
	@NotNull
    private Long mangoId;

    @Column(name="MANGO_TITLE", unique=true)
    @NotNull
    private String mangoTitle;
    

    @Column(name="CVR_IMG_PATH")
    @NotNull
    private String img;
    
    @Column(name="BNR_IMG_PATH")
    @NotNull
    private String bannerImg;
    
    @Column(name="AUTHOR")
    @NotNull
    private String author;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    private MangoStatus status;
    
    @Column(name="LAST_CHAPTER")
    @Nullable
    private Integer lastChapter;
    
    @Column(name="ANILIST_ID")
    @NotNull
    private Long anilistId;
    

    public Optional<Integer> getLastChapter(){
    	return Optional.ofNullable(lastChapter);
    }

}

