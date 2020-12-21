package ua.tarastom.newsaggregator.models;

import android.content.Context;

import java.util.List;

public class ArticleLab {
    private static ArticleLab sArticleLab;
    private static List<Article> articleList;
    private final Context mContext;

    public static ArticleLab get(Context context) {
        if (sArticleLab == null) {
            sArticleLab = new ArticleLab(context);
        }
        return sArticleLab;
    }

    private ArticleLab(Context context) {
        mContext = context.getApplicationContext();
    }

    public static void addArticle(Article article) {
        articleList.add(article);
    }

    public static Article getArticle(int articleId) {

        for (Article article : articleList) {
            if (article.getId() == articleId) {
                return article;
            }
        }
        return null;
    }

    public static void setArticleList(List<Article> listArticles) {
        articleList = listArticles;
    }

    public static List<Article> getArticleList() {
        return articleList;
    }
}