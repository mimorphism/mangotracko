
package com.mimorphism.mangotracko.mango.mangoinfo.model.anilist;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coverImage",
    "title",
    "staff"
})
@Generated("jsonschema2pojo")
public class Media {

    @JsonProperty("coverImage")
    private CoverImage coverImage;
    @JsonProperty("title")
    private Title title;
    @JsonProperty("staff")
    private Staff staff;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
