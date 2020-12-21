package ua.tarastom.newsaggregator.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class News implements Serializable {
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public class Embedded{
        @SerializedName("articleList")
        @Expose
        private List<Article> articleList;

        public List<Article> getArticles() {
            return articleList;
        }

        public void setArticles(List<Article> articleList) {
            this.articleList = articleList;
        }
    }
}
