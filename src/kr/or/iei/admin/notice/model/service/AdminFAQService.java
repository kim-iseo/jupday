package kr.or.iei.admin.notice.model.service;

import java.util.HashMap;

import kr.or.iei.admin.notice.model.vo.AdminFAQ;

public interface AdminFAQService {
	
	HashMap<String, Object> selectAllFAQPageList(int currentPage);

	int updateFAQDelYN(int faqNo, char delYN);

	AdminFAQ selectOneFAQContent(int faqNo);

	int updateFAQPost(AdminFAQ adfaq);

	int insertFAQWrite(AdminFAQ adf);

	int searchFAQPostNo(AdminFAQ adfwrite);



}
