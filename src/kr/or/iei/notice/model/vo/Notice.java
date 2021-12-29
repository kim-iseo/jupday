package kr.or.iei.notice.model.vo;

import java.sql.Date;

public class Notice {
	

	// MEMBER FIELD
	private int noticeNo;//게시글 번호
	private String noticeWriter;// 2 게시글 작성자
	private String noticeTitle;// 3 게시글 제목
	private String noticeContent;// 4 게시글 내용 
	private Date noticeRegDate;// 5 게시글 작성일자
	private int noticeHits;//6 조회수? 
	private char delYN;// 6 삭제여부 없어도 되나? 

	//INSERT INTO notice VALUES(N_NO_Seq.NEXTVAL,'admin','서버 점검 안내(12/18)','안녕하십니까 줍데이입니다, 줍데이 홈페이지 서버 점검으로 인해 12/18 오전 1시부터 오전 6시까지 서버 점검이 있을 예정입니다, 회원 여러분들의 양해를 바랍니다',SYSDATE,'0');

	public Notice() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//메인페이지에서 공지사항 리스트에 사용하는 생성자
	public Notice(int noticeNo, String noticeTitle, String noticeContent, Date noticeRegDate) {
		super();
		this.noticeNo = noticeNo;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticeRegDate = noticeRegDate;
	}
	
	public Notice(int noticeNo, String noticeWriter, String noticeTitle, String noticeContent, Date noticeRegDate,
			int noticeHits, char delYN) {
		super();
		this.noticeNo = noticeNo;
		this.noticeWriter = noticeWriter;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticeRegDate = noticeRegDate;
		this.noticeHits = noticeHits;
		this.delYN = delYN;
	}

	public int getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(int noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getNoticeWriter() {
		return noticeWriter;
	}

	public void setNoticeWriter(String noticeWriter) {
		this.noticeWriter = noticeWriter;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public Date getNoticeRegDate() {
		return noticeRegDate;
	}

	public void setNoticeRegDate(Date noticeRegDate) {
		this.noticeRegDate = noticeRegDate;
	}

	public int getNoticeHits() {
		return noticeHits;
	}

	public void setNoticeHits(int noticeHits) {
		this.noticeHits = noticeHits;
	}

	public char getDelYN() {
		return delYN;
	}

	public void setDelYN(char delYN) {
		this.delYN = delYN;
	}

	@Override
	public String toString() {
		return "Notice [noticeNo=" + noticeNo + ", noticeWriter=" + noticeWriter + ", noticeTitle=" + noticeTitle
				+ ", noticeContent=" + noticeContent + ", noticeRegDate=" + noticeRegDate + ", noticeHits=" + noticeHits
				+ ", delYN=" + delYN + "]";
	}
	
	

}
