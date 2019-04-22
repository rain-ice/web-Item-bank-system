package shiyan.table;

public class Paper {
	private int id;
	private int teacherid;
	private int ruleid;
	private String tittle;
	private int releasetype;
	private int automark;
	private String create_date;
	private Rule rule;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTeacherid() {
		return teacherid;
	}
	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
	public int getRuleid() {
		return ruleid;
	}
	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	public int getReleasetype() {
		return releasetype;
	}
	public void setReleasetype(int releasetype) {
		this.releasetype = releasetype;
	}
	public int getAutomark() {
		return automark;
	}
	public void setAutomark(int automark) {
		this.automark = automark;
	}
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	
	

}
