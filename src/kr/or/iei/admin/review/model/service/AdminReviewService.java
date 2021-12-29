package kr.or.iei.admin.review.model.service;

import java.util.HashMap;

public interface AdminReviewService {

	HashMap<String, Object> selectAllReviewPageList(int currentPage);

	int updateReviewDelYN(int postnum, char delYN);

	int updateReviewBestYN(int postnum, char bestYN);

}
