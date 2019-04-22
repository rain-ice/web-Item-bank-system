package shiyan.table;

public class Question {
	private int id;
	private int glevel;
	private int type;
	private String question;
	private int pointid;
	private String answer_a;
	private String answer_b;
	private String answer_c;
	private String answer_d;
	private String create_date;
	private String answer;
	private int teacherid;
	private int number;
	/**
	 * 后期加入，由老师决定的分数
	 */
	protected double score;
	
	//下面字段方便数据提取
	protected String username;
	protected String pointname;
	protected String level;
	protected String typename;
	
	
	
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPointname() {
		return pointname;
	}
	public void setPointname(String pointname) {
		this.pointname = pointname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getTeacherid() {
		return teacherid;
	}
	public void setTeacherid(int teacherid) {
		this.teacherid = teacherid;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", glevel=" + glevel + ", type=" + type + ", question=" + question + ", pointid="
				+ pointid + ", answer_a=" + answer_a + ", answer_b=" + answer_b + ", answer_c=" + answer_c
				+ ", answer_d=" + answer_d + ", create_date=" + create_date + ", answer=" + answer + ", teacherid="
				+ teacherid + ", number=" + number + ", score=" + score + ", username=" + username + ", pointname="
				+ pointname + ", level=" + level + ", typename=" + typename + "]";
	}
	
	
	

}
