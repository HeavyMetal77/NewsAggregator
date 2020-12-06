package ua.tarastom.newsaggregator.utils.parsers;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ua.tarastom.newsaggregator.models.Article;

public class ParserRss5Channel implements Callable {
    private final String CHANNEL = "channel";
    private final String PUB_DATE = "pubDate";
    private final String DESCRIPTION = "description";
    private final String LINK = "link";
    private final String TITLE = "title";
    private final String ITEM = "item";
    private final String URL_TO_IMAGE = "enclosure";
    private final String CATEGORY = "category";
    private List<Article> articles;
    private final String URL_RSS;

    public ParserRss5Channel(String URL_RSS) {
        this.URL_RSS = URL_RSS;
    }


    public void parse(String urlRss) {
        articles = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = null;
        try {
            stream = new URL(urlRss).openConnection().getInputStream();
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            Article item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new Article();
                            item.setSource("5.ua");
                            item.setAuthor("5 канал");
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                item.setUrl(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");
                                item.setDescription(parser.nextText().trim());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                Log.i("Attribute", "date");
                                item.setPublishedAt(parser.nextText());
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                item.setTitle(parser.nextText().trim());
                            }else if (name.equalsIgnoreCase(CATEGORY)) {
                                Log.i("Attribute", "category");
                                item.getCategory().add(parser.nextText().trim());
                            }else if (name.equalsIgnoreCase(URL_TO_IMAGE)&&parser.getAttributeValue(1).equals("image/jpeg")) {
                                Log.i("Attribute", "urlToImage");
                                parser.getAttributeValue(0); //"address jpg"
                                item.setUrlToImage(parser.getAttributeValue(0));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                            articles.add(item);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object call() throws Exception {
        parse(URL_RSS);
        return articles;
    }
}
