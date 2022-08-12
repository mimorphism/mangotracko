
package com.mimorphism.mangotracko.mango.mangoinfo.anilist;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mimorphism.mangotracko.mango.MangoStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coverImage",
    "bannerImage",
    "title",
    "staff",
    "status",
    "chapters"
})
@Generated("jsonschema2pojo")
public class Media {

    @JsonProperty("coverImage")
    private CoverImage coverImage;
    @JsonProperty("bannerImage")
    private String bannerImage;
	@JsonProperty("title")
    private Title title;
    @JsonProperty("staff")
    private Staff staff;
    @JsonProperty("status")
    private MangoStatus status;
    @JsonProperty("chapters")
    private Integer lastChapter;
    @JsonProperty("id")
    private Long anilistId;
    

	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("coverImage")
    public CoverImage getCoverImage() {
        return coverImage;
    }

    @JsonProperty("coverImage")
    public void setCoverImage(CoverImage coverImage) {
        this.coverImage = coverImage;
    }

    @JsonProperty("title")
    public Title getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(Title title) {
        this.title = title;
    }

    @JsonProperty("staff")
    public Staff getStaff() {
        return staff;
    }

    @JsonProperty("staff")
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    @JsonProperty("bannerImage")
    public String getBannerImage() {
		return bannerImage;
	}

    @JsonProperty("bannerImage")
	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}
    
    @JsonProperty("status")
    public MangoStatus getStatus() {
		return status;
	}

    @JsonProperty("status")
    public MangoStatus setStatus(MangoStatus status) {
		return this.status = status;
	}
    
    @JsonProperty("chapters")
    public Integer getLastChapter() {
		return lastChapter;
	}

    @JsonProperty("chapters")
	public void setLastChapter(Integer lastChapter) {
		this.lastChapter = lastChapter;
	}
    
    @JsonProperty("id")
    public Long getAnilistId() {
		return anilistId;
	}
    
    @JsonProperty("id")
	public void setAnilistId(Long anilistId) {
		this.anilistId = anilistId;
	}



    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
