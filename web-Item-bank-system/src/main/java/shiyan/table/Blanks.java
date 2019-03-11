package shiyan.table;

public class Blanks {

	private int id;
	private int glevel;
	private String question;
	private int pointid;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String create_date;
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
	public String getAnswer1() {
		return answer1;
	}
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	public String getAnswer2() {
		return answer2;
	}
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	public String getAnswer3() {
		return answer3;
	}
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}
	public String getAnswer4() {
		return answer4;
	}
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	@Override
	public String toString() {
		return "Blanks [id=" + id + ", glevel=" + glevel + ", question=" + question + ", pointid=" + pointid
				+ ", answer1=" + answer1 + ", answer2=" + answer2 + ", answer3=" + answer3 + ", answer4=" + answer4
				+ ", create_date=" + create_date + "]";
	}
	
	
	
}
