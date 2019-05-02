package shiyan.table;

public class Dopaper {
	private int id;
	private int studentid;
	private int paperid;
	private double score;
	private String create_date;
	private int type;
	private Paper paper;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStudentid() {
		return studentid;
	}
	public void setStudentid(int studentid) {
		this.studentid = studentid;
	}
	public int getPaperid() {
		return paperid;
	}
	public void setPaperid(int paperid) {
		this.paperid = paperid;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Paper getPaper() {
		return paper;
	}
	public void setPaper(Paper paper) {
		this.paper = paper;
	}
	@Override
	public String toString() {
		return "Dopaper [id=" + id + ", studentid=" + studentid + ", paperid=" + paperid + ", score=" + score
				+ ", create_date=" + create_date + ", type=" + type + ", paper=" + paper + "]";
	}
	
	

}
