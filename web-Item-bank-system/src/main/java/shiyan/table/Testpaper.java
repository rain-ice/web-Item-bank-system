package shiyan.table;

public class Testpaper {
	private int id;
	private int teacherid;
	private int paperid;
	private int questionid;
	private int questiontype;
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
	public int getPaperid() {
		return paperid;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	public int getQuestiontype() {
		return questiontype;
	}
	public void setQuestiontype(int questiontype) {
		this.questiontype = questiontype;
	}
	@Override
	public String toString() {
		return "Testpaper [id=" + id + ", teacherid=" + teacherid + ", paperid=" + paperid + ", questionid="
				+ questionid + ", questiontype=" + questiontype + "]";
	}
	
	

}
