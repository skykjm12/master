package first.common.util;

import java.util.Map;

public class PagingUtils {
	
	private static final int countPerPage = 10;
	private static final int unitPage = 10;

	/** 페이지 정보 셋팅 **/
	public static Map setPageInfo(Map reqParam, int defaultCountPerPage) {
		int pageNo = Integer.parseInt( reqParam.get("pageNo").toString() );  //java Object -> int 형 변환
		int countPerPage = Integer.parseInt( reqParam.get("countPerPage").toString() );

		countPerPage = countPerPage < 100 ? countPerPage : 100; // 최대 100개로 제한

		int first = ((pageNo - 1) * countPerPage) + 1, last = first + countPerPage - 1;

		reqParam.put("countPerPage", countPerPage);
		reqParam.put("first", first);
		reqParam.put("last", last);
		
		System.out.println("setPageInfo  :::: "+reqParam);
		
		return reqParam;
	}
	
	/** 페이징 처리 (파라미터 2개) **/
	public static Map getPageObject(int totalCount, int currentPageNo)
	{
		return getPageObject(totalCount, currentPageNo, 10);
	}
	
	/** 페이징 처리 (파라미터 3개) **/
	public static Map getPageObject(int totalCount, int currentPageNo, int countPerPage)
	{
		return getPageObject(totalCount, currentPageNo, countPerPage, 10);
	}
	
	/** 페이징 처리 (파라미터 4개) **/
	public static Map getPageObject(int totalCount, int currentPageNo, int countPerPage, int unitPage) {
		int currPage = currentPageNo;
		int unitCount = 100;

		boolean isFirst = false;

		if (totalCount == 0) {
			countPerPage = unitCount;
		} else if (totalCount < countPerPage) {
			countPerPage = totalCount / unitCount * unitCount;
			if (totalCount % unitCount > 0) {
				countPerPage += unitCount;
			}
		}

		int totalPage = getMaxNum(totalCount, countPerPage);

		if (totalPage < currPage)
			currPage = totalPage;
		
		int currStartCount;
		int currEndCount;

		if (currPage != 1) {
			currEndCount = currPage * countPerPage;
			currStartCount = currEndCount - countPerPage;
		} else {
			currEndCount = countPerPage;
			currStartCount = 0;
		}

		if (currEndCount > totalCount)
			currEndCount = totalCount;
		
		int currStartPage;
		int currEndPage;

		if (totalPage <= unitPage) {
			currEndPage = totalPage;
			currStartPage = 1;
		} else {
			currEndPage = (currPage - 1) / unitPage * unitPage + unitPage;
			currStartPage = currEndPage - unitPage + 1;
		}

		if (currEndPage > totalPage)
			currEndPage = totalPage;

		int prePage;
		boolean prePage_is;
		if (currStartPage != 1) {
			prePage_is = true;
			prePage = currStartPage - 1;

		} else {
			prePage_is = false;
			prePage = 0;
		}
		int nextPage;
		boolean nextPage_is;

		if (currEndPage != totalPage) {
			nextPage_is = true;
			nextPage = currEndPage + 1;
		} else {
			nextPage_is = false;
			nextPage = 0;
		}

		Map tempJSON = new java.util.HashMap();
		try {
			tempJSON.put("currPage", Integer.valueOf(currPage));
			tempJSON.put("unitPage", Integer.valueOf(unitPage));
			tempJSON.put("prePage", Integer.valueOf(prePage));
			tempJSON.put("prePage_is", Boolean.valueOf(prePage_is));
			tempJSON.put("nextPage", Integer.valueOf(nextPage));
			tempJSON.put("nextPage_is", Boolean.valueOf(nextPage_is));
			tempJSON.put("currStartPage", Integer.valueOf(currStartPage));
			tempJSON.put("currEndPage", Integer.valueOf(currEndPage));

			tempJSON.put("totalCount", Integer.valueOf(totalCount));
			tempJSON.put("totalPage", Integer.valueOf(totalPage));
		} catch (Exception localException) {
		}
		return tempJSON;
	}

	/** max 페이지 구하기 **/
	private static int getMaxNum(int allPage, int list_num) {
		if (allPage % list_num == 0) {
			return allPage / list_num;
		}
		return allPage / list_num + 1;
	}

}

