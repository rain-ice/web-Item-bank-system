package shiyan.table;

public class Dotestpaper {
	private int id;
	private int dopaperid;
	private int testpaperid;
	private String answer;
	private String answer_a;
	private String answer_b;
	private String answer_c;
	private String answer_d;
	private double score;
	
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
	public int getDopaperid() {
		return dopaperid;
	}
	public void setDopaperid(int dopaperid) {
		this.dopaperid = dopaperid;
	}

	public int getTestpaperid() {
		return testpaperid;
	}
	public void setTestpaperid(int testpaperid) {
		this.testpaperid = testpaperid;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
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
	@Override
	public String toString() {
		return "Dotestpaper [id=" + id + ", dopaperid=" + dopaperid + ", testpaperid=" + testpaperid + ", answer="
				+ answer + ", answer_a=" + answer_a + ", answer_b=" + answer_b + ", answer_c=" + answer_c
				+ ", answer_d=" + answer_d + "]";
	}
			

}
