package ua.tarastom.newsaggregator.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "rss", strict = false)
public class ArticleResponse {

    @Element(name = "channel")
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    @Root(name = "channel", strict = false)
    public static class Channel {

        @Path("title")
        @Text(required = false)
        private String titleChannel;

        @Element(name = "language")
        private String language;

        @Element(name = "pubDate")
        private String pubDateChannel;

        @ElementList(inline = true, required = false)
        private List<Article> articles;

        public List<Article> getArticles() {
            for (Article article : articles) {
                article.setTitleChannel(titleChannel);
                article.setLanguage(language);
            }
            return articles;
        }

        public String getTitle() {
            return titleChannel;
        }

        public String getPubDateChannel() {
            return pubDateChannel;
        }

        public void setPubDateChannel(String pubDateChannel) {
            this.pubDateChannel = pubDateChannel;
        }

        public void setTitleChannel(String titleChannel) {
            this.titleChannel = titleChannel;
        }
    }

}