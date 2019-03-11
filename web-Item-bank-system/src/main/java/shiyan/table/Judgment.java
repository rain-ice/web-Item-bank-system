package shiyan.table;

public class Judgment {
	
	private int id;
	private int glevel;
	private String question;
	private int pointid;
	private String create_date;
	private int answer;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGlevel() {
		return glevel;
	}
	public void setGlevel(int glevel) {
		this.glevel = glevel;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public int getPointid() {
		return pointid;
	}
	public void setPointid(int pointid) {
		this.pointid = pointid;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	@Override
	public String toString() {
		return "Judgment [id=" + id + ", glevel=" + glevel + ", question=" + question + ", pointid=" + pointid
				+ ", create_date=" + create_date + ", answer=" + answer + "]";
	}
	

}
