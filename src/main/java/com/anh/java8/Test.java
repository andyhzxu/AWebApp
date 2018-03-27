package com.anh.java8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * java类简单作用描述
 *
 * @Description: java类作用描述
 * @Author: anh6
 * @CreateDate: 2018/3/10 10:42
 * @UpdateUser:
 * @UpdateRemark:
 */
public class Test {
    public static void main(String[] args) {
        Article firstJavaArticle = getFirstJavaArticle();
    }

    public static Article getFirstJavaArticle() {
        List<Article> articles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getTags().contains("Java")) {
                return article;
            }
        }

        return null;
    }

    public static Optional<Article> getFirstJavaArticle2() {
        List<Article> articles = new ArrayList<>();
        return articles.stream().filter(article -> article.getTags().contains("java")).findFirst();
    }

    public static List<Article> getFirst() {
        List<Article> articles = new ArrayList<>();
        return articles.stream().filter(article -> article.getTags().contains("java")).collect(Collectors.toList());
    }
}
