package shiyan.table;

public class Mistakes {
	private int id;
	private int studentid;
	private int questionid;
	private Question question;
	private Dorecord dorecord;
	
	
	public Dorecord getDorecord() {
		return dorecord;
	}
	public void setDorecord(Dorecord dorecord) {
		this.dorecord = dorecord;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
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
	@Override
	public String toString() {
		return "Mistakes [id=" + id + ", studentid=" + studentid + ", questionid=" + questionid + ", question="
				+ question + "]";
	}
	

}
