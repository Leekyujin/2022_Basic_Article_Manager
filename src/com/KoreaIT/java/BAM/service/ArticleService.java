package com.KoreaIT.java.BAM.service;

import java.util.List;

import com.KoreaIT.java.BAM.Container.Container;
import com.KoreaIT.java.BAM.dto.Article;

public class ArticleService {

	public List<Article> getForPrintArticles(String searchKeyword) {

		List<Article> articles = Container.articleDao.getArticles(searchKeyword);

		return articles;
	}

}