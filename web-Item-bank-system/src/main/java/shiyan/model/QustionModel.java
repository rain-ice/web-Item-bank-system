package shiyan.model;

import java.util.ArrayList;
import java.util.List;

import shiyan.table.Question;

public class QustionModel {
	private int pointid=0;
	private int teacherid=0;
	
	
	
	private int page=1;     
	private int pagesize=10;
	private long total=0;
	private int totalpage=0;
	
	private List<Question> list;
	private String error;
	
	public QustionModel(){
		list= new ArrayList<Question>();
		error="";
		
	}
	

	public int getTeacherid() {
		return teacherid;
	}


	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}


	public int getPointid() {
		return pointid;
	}

	public void setPointid(int pointid) {
		this.pointid = pointid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public List<Question> getList() {
		return list;
	}

	public void setList(List<Question> list) {
		this.list = list;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "QustionModel [pointid=" + pointid + ", page=" + page + ", pagesize=" + pagesize + ", total=" + total
				+ ", totalpage=" + totalpage + ", list=" + list + ", error=" + error + "]";
	}
	
	

}
