package utils;

import java.util.List;

public class Page<E> {

	private int rowTotal;// 总记录数
	private int pageSize = 10;// 默认每页记录数10条
	private int currentPage;// 当前页码
	private int beginIndex;//起始记录下标 
	private int totalPage;//总页数
	private List<E> contentList;
	public Page(List<E> contentList, Integer rowTotal, Integer pageSize, Integer currentPage) {
		
		this.contentList = contentList;
		this.pageSize = pageSize;
		this.rowTotal = rowTotal;
		if(rowTotal>0){
			if(currentPage ==null || currentPage<1){
				this.currentPage =1;
			}else{
				this.currentPage = currentPage;
			}
			this.beginIndex = (this.currentPage - 1) * pageSize ; 
			this.totalPage = (rowTotal-1)/pageSize+1;
		}else{
			this.totalPage =1 ;
			this.currentPage =1;
		}
	}

	public int getRowTotal() {
		return rowTotal;
	}
	public void setRowTotal(int rowTotal) {
		this.rowTotal = rowTotal;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<E> getContentList() {
		return contentList;
	}

	public void setContentList(List<E> contentList) {
		this.contentList = contentList;
	}
}
