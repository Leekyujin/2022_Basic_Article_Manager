package com.KoreaIT.java.BAM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.BAM.Container.Container;
import com.KoreaIT.java.BAM.dto.Article;
import com.KoreaIT.java.BAM.dto.Member;
import com.KoreaIT.java.BAM.service.ArticleService;
import com.KoreaIT.java.BAM.service.MemberService;
import com.KoreaIT.java.BAM.util.Util;

public class ArticleController extends Controller{
	private Scanner sc;
	private String cmd;
	private String actionMethodName;
	private ArticleService articleService;
	private MemberService memberService;

	public void doAction(String cmd, String actionMethodName) {
		this.cmd = cmd;
		this.actionMethodName = actionMethodName;

		switch(actionMethodName) {
		case "list":
			showList();
			break;
		case "write":
			doWrite();
			break;
		case "detail":
			showDetail();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		default:
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}

	public ArticleController(Scanner sc) {
		this.sc = sc;

		articleService = Container.articleService;
		memberService = Container.memberService;
	}

	private void showList() {
		String searchKeyword = cmd.substring("article list".length()).trim();

		List<Article> forPrintArticles = articleService.getForPrintArticles(searchKeyword);

		if (forPrintArticles.size() == 0) {
			System.out.println("게시물이 없습니다.");
			return;
		}

		System.out.println("== 게시물 목록 ==");
		System.out.printf("번호  |     제목     |        %5s          |   작성자   |  조회\n", "날짜");

		for (int i = forPrintArticles.size() - 1; i >= 0; i--) {
			Article article = forPrintArticles.get(i);

			String writerName = memberService.getMemberNameById(article.memberId);

			System.out.printf("%3d   |    %4s    |   %5s   |  %4s   | %5d\n", article.id, article.title,
					article.regDate, writerName, article.hit);
		}
	}

	private void doWrite() {
		int id = articleService.setNewId();

		String regDate = Util.getNowDateStr();

		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목 : ");
		String title = sc.nextLine();

		System.out.printf("내용 : ");
		String body = sc.nextLine();

		Article article = new Article(id, regDate, loginedMember.id, title, body);
		articleService.add(article);

		System.out.printf("%d번 글이 생성되었습니다.\n", id);
	}

	private void showDetail() {
		String[] cmdBits = cmd.split(" ");
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요.");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		String writerName = memberService.getMemberNameById(foundArticle.memberId);

		foundArticle.increaseHit();

		System.out.printf("== %d번 게시물 상세보기 ==\n", foundArticle.id);
		System.out.printf("번호 : %d\n", foundArticle.id);
		System.out.printf("날짜 : %s\n", foundArticle.regDate);
		System.out.printf("제목 : %s\n", foundArticle.title);
		System.out.printf("내용 : %s\n", foundArticle.body);
		System.out.printf("작성자 : %s\n", writerName);
		System.out.printf("조회 : %d\n", foundArticle.hit);
	}

	private void doModify() {
		String[] cmdBits = cmd.split(" ");
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요.");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("게시물 수정 권한이 없습니다.");
			return;
		}

		System.out.println("== 게시물 수정 ==");
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		foundArticle.title = title;
		foundArticle.body = body;

		System.out.printf("%d번 게시물을 수정했습니다.\n", id);
	}

	private void doDelete() {
		String[] cmdBits = cmd.split(" ");
		
		if (cmdBits.length == 2) {
			System.out.println("명령어를 확인해주세요.");
			return;
		}

		int id = Integer.parseInt(cmdBits[2]);

		Article foundArticle = articleService.getArticleById(id);

		if (foundArticle == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		if(foundArticle.memberId != loginedMember.id) {
			System.out.println("게시물 삭제 권한이 없습니다.");
			return;
		}
		
		System.out.println("== 게시물 삭제 ==");

		articleService.remove(foundArticle);
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}
	
	public void makeTestData() {
		System.out.println("테스트를 위한 게시물 데이터를 생성합니다.");

		articleService.add(new Article(articleService.setNewId(), Util.getNowDateStr(), 1, "제목1", "내용1", 11));
		articleService.add(new Article(articleService.setNewId(), Util.getNowDateStr(), 2, "제목2", "내용2", 22));
		articleService.add(new Article(articleService.setNewId(), Util.getNowDateStr(), 2, "제목3", "내용3", 33));
	}

}
