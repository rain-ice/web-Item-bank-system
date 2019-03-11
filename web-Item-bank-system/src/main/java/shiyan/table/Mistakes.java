package shiyan.table;

public class Mistakes {
	private int id;
	private int studentid;
	private int questionid;
	private int type;
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
	@Override
	public String toString() {
		return "Mistakes [id=" + id + ", studentid=" + studentid + ", questionid=" + questionid + ", type=" + type
				+ "]";
	}
	

}
