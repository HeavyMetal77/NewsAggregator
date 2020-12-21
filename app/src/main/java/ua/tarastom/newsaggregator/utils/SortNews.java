package ua.tarastom.newsaggregator.utils;

import java.util.ArrayList;
import java.util.List;

import ua.tarastom.newsaggregator.models.Article;

public class SortNews {
    public static List<Article> sortBySource(List<Article> articles, String nameSource) {
        List<Article> sortArticles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getSource().equals(nameSource)) {
                sortArticles.add(article);
            }
        }
        return sortArticles;
    }

    public static List<Article> sortByCategory(List<Article> articles, String category) {
        List<Article> sortArticles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getCategory().equals(category)) {
                sortArticles.add(article);
            }
        }
        return sortArticles;
    }

    public static List<Article> findKeyword(List<Article> articles, String findKeyword) {
        List<Article> sortArticles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getTitle().contains(findKeyword)
                    ||article.getDescription().contains(findKeyword)
                    || article.getCategory().contains(findKeyword)) {
                sortArticles.add(article);
            }
        }
        return sortArticles;
    }
}
