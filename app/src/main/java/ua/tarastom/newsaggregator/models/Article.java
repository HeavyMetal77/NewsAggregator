package ua.tarastom.newsaggregator.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import ua.tarastom.newsaggregator.utils.Utils;

public class Article implements Comparable<Article>, Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("titleChannel")
    @Expose
    private String titleChannel;

    @SerializedName("source")
    @Expose
    private String source;

    //    @JsonDeserialize(using = DateHandler.class)
    @SerializedName("pubDateChannel")
    @Expose
    private String pubDateChannel;

    @SerializedName("logo")
    @Expose
    private String logo;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("guid")
    @Expose
    private String guid;

    //    @JsonDeserialize(using = DateHandler.class)
    @SerializedName("pubDate")
    @Expose
    private String pubDate;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("full_text")
    @Expose
    private String full_text;

    @SerializedName("enclosure")
    @Expose
//    @ElementList(name = "enclosure", required = false)
    private List<String> enclosure;

    @SerializedName("category")
    @Expose
//    @ElementList(name = "category", required = false)
    private List<String> category;

    public Article() {

    }

    public String getSource() {
        String baseUrl = "";
        if (link.startsWith("https://www.")) {
            baseUrl = link.substring(0, link.indexOf('/', link.indexOf("//") + 2)).replace("https://www.", "");
        } else if (link.startsWith("http://www.")) {
            baseUrl = link.substring(0, link.indexOf('/', link.indexOf("//") + 2)).replace("http://www.", "");
        } else if (link.startsWith("https://")) {
            baseUrl = link.substring(0, link.indexOf('/', link.indexOf("//") + 2)).replace("https://", "");
        } else if (link.startsWith("http://")) {
            baseUrl = link.substring(0, link.indexOf('/', link.indexOf("//") + 2)).replace("http://", "");
        }
        return baseUrl;
    }

//    @Path("title")
//    @Text(required = false)

    public String getLink() {
        if (!link.startsWith("https://www.") && link.startsWith("https://")) {
            link = link.replace("https://", "https://www.");
        }
        return link;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int compareTo(Article article) {
        Date date1 = Utils.getDate(this.getPubDate());
        Date date2 = Utils.getDate(article.getPubDate());
        return date1.compareTo(date2);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTitleChannel() {
        return titleChannel;
    }

    public void setTitleChannel(String titleChannel) {
        this.titleChannel = titleChannel;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPubDateChannel() {
        return pubDateChannel;
    }

    public void setPubDateChannel(String pubDateChannel) {
        this.pubDateChannel = pubDateChannel;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public List<String> getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(List<String> enclosure) {
        this.enclosure = enclosure;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
