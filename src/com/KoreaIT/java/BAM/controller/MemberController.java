package com.KoreaIT.java.BAM.controller;

import java.util.List;
import java.util.Scanner;

import com.KoreaIT.java.BAM.Container.Container;
import com.KoreaIT.java.BAM.dto.Member;
import com.KoreaIT.java.BAM.service.MemberService;
import com.KoreaIT.java.BAM.util.Util;

public class MemberController extends Controller {
	private Scanner sc;
	private List<Member> members;
	private String cmd;
	private String actionMethodName;
	private MemberService memberService;

	public MemberController(Scanner sc) {
		this.sc = sc;

		memberService = Container.memberService;
	}

	public void doAction(String cmd, String actionMethodName) {
		this.cmd = cmd;
		this.actionMethodName = actionMethodName;

		switch (actionMethodName) {
		case "join":
			doJoin();
			break;
		case "login":
			doLogin();
			break;
		case "profile":
			showProfile();
			break;
		case "logout":
			doLogout();
			break;
		default:
			System.out.println("존재하지 않는 명령어입니다.");
			break;
		}
	}

	private void doLogout() {
		loginedMember = null;
		System.out.println("로그아웃 되었습니다.");
	}

	private void showProfile() {
		System.out.println("== 현재 로그인 한 회원 정보 ==");

		System.out.printf("아이디 : %s\n", loginedMember.loginId);
		System.out.printf("이름 : %s\n", loginedMember.name);
	}

	private void doLogin() {
		System.out.println("== 로그인 ==");
		Member member = null;

		while (true) {
			System.out.printf("로그인 아이디 : ");
			String loginId = sc.nextLine();

			member = memberService.getMemberByLoginId(loginId);

			if (loginId.trim().length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}

			if (member == null) {
				System.out.println("존재하지 않는 회원입니다.");
				return;
			}

			while (true) {
				System.out.printf("로그인 비밀번호 : ");
				String loginPw = sc.nextLine();

				if (loginPw.trim().length() == 0) {
					System.out.println("비밀번호를 입력해주세요");
					continue;
				}

				if (member.loginPw.equals(loginPw) == false) {
					System.out.println("비밀번호를 확인해주세요.");
					return;
				}
				break;
			}
			break;
		}

		loginedMember = member;

		System.out.printf("%s님 환영합니다.\n", loginedMember.name);

	}

	private void doJoin() {
		int id = memberService.setNewId();
		String regDate = Util.getNowDateStr();

		String loginId = null;

		System.out.println("== 회원가입 ==");

		while (true) {
			System.out.printf("로그인 아이디 : ");
			loginId = sc.nextLine();

			if (loginId.trim().length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}

			if (memberService.isJoinableLoginId(loginId) == false) {
				System.out.printf("%s은(는) 이미 사용중인 아이디입니다.\n", loginId);
				continue;
			}
			break;
		}

		String loginPw = null;
		String loginPwConfirm = null;

		while (true) {
			System.out.printf("로그인 비밀번호 : ");
			loginPw = sc.nextLine();

			if (loginPw.trim().length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}

			System.out.printf("로그인 비밀번호 확인 : ");
			loginPwConfirm = sc.nextLine();

			if (loginPw.equals(loginPwConfirm) == false) {
				System.out.println("비밀번호를 다시 입력해주세요.");
				continue;
			}
			break;
		}

		String name = null;

		while (true) {
			System.out.printf("이름 : ");
			name = sc.nextLine().trim();

			if (name == "") {
				System.out.println("이름을 입력해주세요.");
				continue;
			}
			break;
		}

		Member member = new Member(id, regDate, loginId, loginPw, name);
		memberService.add(member);

		System.out.printf("%s님의 회원가입이 완료되었습니다.\n", loginId);

	}

	public void makeTestData() {
		System.out.println("테스트를 위한 회원 데이터를 생성합니다.");

		memberService.add(new Member(memberService.setNewId(), Util.getNowDateStr(), "user1", "pw1", "홍길동"));
		memberService.add(new Member(memberService.setNewId(), Util.getNowDateStr(), "user2", "pw2", "이순신"));
		memberService.add(new Member(memberService.setNewId(), Util.getNowDateStr(), "user3", "pw3", "임꺽정"));
	}

}