package ua.tarastom.newsaggregator.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.Date;

import ua.tarastom.newsaggregator.utils.Utils;

@Root(name = "item", strict = false)
public class Article implements Comparable<Article> {

    private String language;

    private String titleChannel;

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

    @Path("title")
    @Text(required = false)
    private String title;

    @Element(name = "link")
    private String link;

    @Element(name = "description")
    private String description;

    @Path("enclosure")
    @Attribute(name = "url", required = false)
    private String enclosure;

    @Element(name = "guid")
    private String guid;

    @Element(name = "pubDate")
    private String pubDate;

//    @ElementList(name = "category", required = false)
//    private List<String> categories;
//
//    public List<String> getCategories() {
//        return categories;
//    }
//
//    public void setCategories(List<String> categories) {
//        this.categories = categories;
//    }

    @Element(name = "category", required = false)
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        if (!link.startsWith("https://www.") && link.startsWith("https://")) {
            link = link.replace("https://", "https://www.");
        }
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTitleChannel(String titleChannel) {
        this.titleChannel = titleChannel;
    }

    public String getTitleChannel() {
        return titleChannel;
    }

    @Override
    public int compareTo(Article article) {
        Date date1 = Utils.getDate(this.getPubDate());
        Date date2 = Utils.getDate(article.getPubDate());
        return date1.compareTo(date2);
    }
}
