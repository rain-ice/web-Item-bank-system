package shiyan.table;

public class Choice {
	private int id;
	private int glevel;
	private String question;
	private int pointid;
	private String answer_a;
	private String answer_b;
	private String answer_c;
	private String answer_d;
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
	public String getAnswer_a() {
		return answer_a;
	}
	public void setAnswer_a(String answer_a) {
		this.answer_a = answer_a;
	}
	public String getAnswer_b() {
		return answer_b;
	}
	public void setAnswer_b(String answer_b) {
		this.answer_b = answer_b;
	}
	public String getAnswer_c() {
		return answer_c;
	}
	public void setAnswer_c(String answer_c) {
		this.answer_c = answer_c;
	}
	public String getAnswer_d() {
		return answer_d;
	}
	public void setAnswer_d(String answer_d) {
		this.answer_d = answer_d;
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
		return "Choice [id=" + id + ", glevel=" + glevel + ", question=" + question + ", pointid=" + pointid
				+ ", answer_a=" + answer_a + ", answer_b=" + answer_b + ", answer_c=" + answer_c + ", answer_d="
				+ answer_d + ", create_date=" + create_date + ", answer=" + answer + "]";
	}
	

}
