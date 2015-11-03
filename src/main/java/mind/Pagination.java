//package mind;
//
//public class Pagination {
//
//
//	private int totalPage;
//	private int pageSize;
//	private int current;
//	private boolean prepage;
//	private boolean nextpage;
//	private int showNum = 5;
//	private int pageFirst;
//	private int pageEnd;
//	private int increase = 2; 
//
//	public Pagination(int pageSize) {
//		this.pageSize = pageSize;
//	}
//
//	public Pagination(int pageSize, int showNum) {
//		this.pageSize = pageSize;
//		this.showNum = showNum;
//	}
//
//	public void build(int total, int currentPage) {
//		this.current = currentPage;
//		totalPage = (total % pageSize == 0) ? (total / pageSize) : (total / pageSize + 1);
//		this.current = this.current <= 0 ? 1 : this.current;
//		this.current = this.current > totalPage ? totalPage : this.current;
//		prepage = this.current > 1;
//		nextpage = this.current < totalPage;
//		show();
//	}
//
//	public void buildPage(int totalPage, int currentPage) {
//		this.totalPage = totalPage;
//		this.current = currentPage;
//		if (this.current < 1) {
//			this.current = 1;
//		}
//		if (this.current > this.totalPage) {
//			this.current = this.totalPage;
//		}
//
//		prepage = this.current > 1;
//		nextpage = this.current < this.totalPage;
//		show();
//	}
//
//	public void show() {
//		if (this.totalPage == 0) {
//			this.pageFirst = 0;
//			this.pageEnd = 0;
//			return;
//		}		
//		
//		if(current<showNum){
//			pageFirst = current - increase <= 1 ? 1 : current - increase;
//			if(totalPage <= showNum){
//				pageEnd = totalPage;
//			}
//			else{
//				if(current +increase <= showNum){
//					pageEnd = current +increase >= totalPage ? totalPage : showNum;
//				}
//				else{
//					pageEnd = current +increase >= totalPage ? totalPage : current +increase;
//				}				
//			}			
//			
//		}
//		else{						
//			pageEnd = current +increase > totalPage ? totalPage : current +increase;
//			if(pageEnd == totalPage){
//				pageFirst = totalPage - 4;
//			}
//			else{
//				pageFirst = current - increase <= 1 ? 1 : current - increase;	
//			}
//					
//		}		
//	}
//
//	public static String addzero(int num, int length) {
//		StringBuffer sb = new StringBuffer();
//		if (num < Math.pow(10, length - 1)) {
//			for (int i = 0; i < (length - (num + "").length()); i++) {
//				sb.append("0");
//			}
//		}
//		sb.append(num);
//		return sb.toString();
//	}
//
//	public int getTotalPage() {
//		return totalPage;
//	}
//
//	public int getPageSize() {
//		return pageSize;
//	}
//
//	public int getCurrent() {
//		return current;
//	}
//
//	public boolean isPrepage() {
//		return prepage;
//	}
//
//	public boolean isNextpage() {
//		return nextpage;
//	}
//
//	public int getShowNum() {
//		return showNum;
//	}
//
//	public int getPageFirst() {
//		return pageFirst;
//	}
//
//	public int getPageEnd() {
//		return pageEnd;
//	}
//
//
//}