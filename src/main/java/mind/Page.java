//package mind;
//import java.util.List;
//
//public class Page<E> {
//
//	// 当前页码
//	final private int pageNo;
//
//	// 当前页大小
//	final private int pageSize;
//
//	// 总页数
//	final private int totalPageNum;
//
//	// 总记录数
//	final private int totalNum;
//
//	// 实体List
//	final private List<E> content;
//
//	public Page(List<E> content, int pageNo, int pageSize, int totalPageNum,
//			int totalNum) {
//		this.content = content;
//		this.pageNo = pageNo;
//		this.pageSize = pageSize;
//		this.totalPageNum = totalPageNum;
//		this.totalNum = totalNum;
//	}
//
//	public int getContentSize() {
//		return this.content.size();
//	}
//
//	public List<E> getPageContent() {
//		return this.content;
//	}
//
//	public int getPageNo() {
//		return this.pageNo;
//	}
//
//	public int getPageSize() {
//		return this.pageSize;
//	}
//
//	public int getTotalPageNum() {
//		return this.totalPageNum;
//	}
//
//	public int getTotalNum() {
//		return totalNum;
//	}
//
//}