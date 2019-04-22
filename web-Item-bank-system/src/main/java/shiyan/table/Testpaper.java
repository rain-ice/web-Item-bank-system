package shiyan.table;

public class Testpaper {
	private int id;
	private int teacherid;
	private int paperid;
	private int questionid;
	private double score;
	private Question question;
	

	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
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
	@Override
	public String toString() {
		return "Testpaper [id=" + id + ", teacherid=" + teacherid + ", paperid=" + paperid + ", questionid="
				+ questionid + "]";
	}

	
	
	

}
