package shiyan.table;

public class Dorecord {
	
	private int id;
	private int studentid;
	private int questionid;
	private int type;
	private int yes;
	private int no;
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
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getYes() {
		return yes;
	}
	public void setYes(int yes) {
		this.yes = yes;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	@Override
	public String toString() {
		return "Dotitle [id=" + id + ", studentid=" + studentid + ", questionid=" + questionid + ", type=" + type
				+ ", yes=" + yes + ", no=" + no + "]";
	}
	

}
