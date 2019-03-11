package shiyan.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询的参数和结果集对象
 * @author iceki
 *
 */
public class Pagination<T> {
	/**
	 * 当前页码
	 * 页码从1开始
	 * page<=0 或者 pagesize<=0 则表示不分页
	 */
	private int page;
	
	/**
	 * 每页数量
	 */
	private int pagesize;
	
	/**
	 * 满足条件的记录总数
	 */
	private long total;
	
	/**
	 * 总页数
	 */
	private int totalpage;
	
	/**
	 * 记录的起始位置，从0开始
	 * (page-1)*pagesize
	 */
	private int startindex;
	
	/**
	 * 本页记录的数量
	 */
	private int rowsize;
	
	/**
	 * 记录的结束位置
	 * startindex+recordsize;
	 */
	private int endindex;
	
	/**
	 * 本页的记录数组
	 */
	private List<T> rows;
	
	/**
	 * 添加where语句
	 */
	private StringBuffer where;
	
	/**
	 * where中如果包含了 ? 号，可以添加值
	 */
	private List<Object> whereValue;
	
	/**
	 * 添加orderby语句
	 */
	private StringBuffer orderby;
	
	public Pagination(){
		this.page = 0;
		this.pagesize = 0;
		this.total = 0l;
		this.totalpage=0;
		this.startindex=0;
		this.rowsize=0;
		this.endindex=0;
	}
	
	public Pagination(int page,int pagesize) {
		this();
		this.page = page;
		this.pagesize = pagesize;
		if(needPaging()) {
			//需要分页时候，计算记录的起始位置
			this.startindex = (this.page-1)*this.pagesize;
		}
	}
	
	/**
	 * 是否需要分页
	 * @return
	 */
	public boolean needPaging() {
		if(page>0 && pagesize>0)
			return true;
		return false;
	}
	
	/**
	 * 计算总页数
	 */
	private void caculateTotalpage() {
		if(total<=0 || pagesize<=0) {
			return;
		}
		totalpage = (int)(total / pagesize);
		if(total % pagesize != 0)
			totalpage++;
	}
	
	/**
	 * 组装where语句，必须以where开始
	 * @param str
	 * @return
	 */
	public StringBuffer appendWhere(String str) {
		if(where==null) {
			where = new StringBuffer(" ");
		}
		return where.append(str);
	}
	
	/**
	 * 产生最终的where语句
	 * @return
	 */
	public String gnrWhere() {
		if(where == null)
			return "";
		
		String str = where.toString();
		
		if(str.indexOf("where")!=-1)
			return str;
		
		if(str.indexOf("WHERE")!=-1)
			return str;
		
		return "";
	}
	
	/**
	 * 组装 order by 语句，必须以order by 开始
	 * @param str
	 * @return
	 */
	public StringBuffer appendOrderBy(String str) {
		if(orderby==null) {
			orderby = new StringBuffer(" ");
		}
		return orderby.append(str);
	}
	
	/**
	 * 产生最终的order by 语句
	 * @return
	 */
	public String gnrOrderBy() {
		if(orderby == null)
			return "";
		
		String str = orderby.toString();
		
		if(str.indexOf("order by")!=-1)
			return str;
		
		if(str.indexOf("ORDER BY")!=-1)
			return str;
		
		return "";
	}
	
	/**
	 * 添加where语句中包含的 ? 的值
	 * @param value
	 */
	public void addWhereValue(Object value) {
		if(whereValue==null) {
			whereValue = new ArrayList<>();
		}
		whereValue.add(value);
	}
	
	public Object[] gnrWhereValues() {
		if(whereValue==null || whereValue.isEmpty())
			return null;
		return whereValue.toArray(new Object[] {});
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		if(needPaging()) {
			//需要分页时候，计算记录的起始位置
			this.startindex = (this.page-1)*this.pagesize;
		}
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
		if(needPaging()) {
			//需要分页时候，计算记录的起始位置
			this.startindex = (this.page-1)*this.pagesize;
		}
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
		caculateTotalpage();
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

	public int getRowsize() {
		return rowsize;
	}

	public void setRowsize(int rowsize) {
		this.rowsize = rowsize;
		if(this.rowsize>0)
			this.endindex = this.startindex+this.rowsize-1;
	}


	public int getEndindex() {
		return endindex;
	}

	public void setEndindex(int endindex) {
		this.endindex = endindex;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
