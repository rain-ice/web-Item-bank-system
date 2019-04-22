package shiyan.table;

import java.sql.Date;
import java.util.List;

/**
 * 用于接收前端传送的规则
 * @author anstar
 *
 */
public class Rule {
	/**
	 * 规则的ID
	 */
	private int id=0;
	/**
     * 试卷总分
     */
    private int totalMark=0;
    /**
     * 试卷期望难度系数
     */
    private double level=0;
    /**
     * 单选题数量
     */
    private int singleNum=0;
    /**
     * 单选题单个分值
     */
    private double singleScore=0;
    /**
     * 判断题数量
     */
    private int JudgmentNum=0;
    /**
     * 判断题单个分值
     */
    private double JudgmentScore=0;
    /**
     * 填空题数量
     */
    private int completeNum=0;
    /**
     * 填空题单个分值
     */
    private double completeScore=0;
    /**
     * 主观题数量
     */
    private int subjectiveNum=0;
    /**
     * 主观题单个分值
     */
    private double subjectiveScore=0;
    /**
     * 试卷包含的知识点id
     */
    private List<String> pointIds;
    /**
     * 制定需要存储的类型
     */
    private String idpack;
	public int getTotalMark() {
		return totalMark;
	}
	public void setTotalMark(int totalMark) {
		this.totalMark = totalMark;
	}
	public double getLevel() {
		return level;
	}
	public void setLevel(double level) {
		this.level = level;
	}
	public int getSingleNum() {
		return singleNum;
	}
	public void setSingleNum(int singleNum) {
		this.singleNum = singleNum;
	}
	public double getSingleScore() {
		return singleScore;
	}
	public void setSingleScore(double singleScore) {
		this.singleScore = singleScore;
	}
	public int getJudgmentNum() {
		return JudgmentNum;
	}
	public void setJudgmentNum(int judgmentNum) {
		JudgmentNum = judgmentNum;
	}
	public double getJudgmentScore() {
		return JudgmentScore;
	}
	public void setJudgmentScore(double judgmentScore) {
		JudgmentScore = judgmentScore;
	}
	public int getCompleteNum() {
		return completeNum;
	}
	public void setCompleteNum(int completeNum) {
		this.completeNum = completeNum;
	}
	public double getCompleteScore() {
		return completeScore;
	}
	public void setCompleteScore(double completeScore) {
		this.completeScore = completeScore;
	}
	public int getSubjectiveNum() {
		return subjectiveNum;
	}
	public void setSubjectiveNum(int subjectiveNum) {
		this.subjectiveNum = subjectiveNum;
	}
	public double getSubjectiveScore() {
		return subjectiveScore;
	}
	public void setSubjectiveScore(double subjectiveScore) {
		this.subjectiveScore = subjectiveScore;
	}
	public List<String> getPointIds() {
		return pointIds;
	}
	public void setPointIds(List<String> pointIds) {
		this.pointIds = pointIds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdpack() {
		return idpack;
	}
	public void setIdpack(String idpack) {
		this.idpack = idpack;
	}
	@Override
	public String toString() {
		return "Rule [id=" + id + ", totalMark=" + totalMark + ", level=" + level + ", singleNum=" + singleNum
				+ ", singleScore=" + singleScore + ", JudgmentNum=" + JudgmentNum + ", JudgmentScore=" + JudgmentScore
				+ ", completeNum=" + completeNum + ", completeScore=" + completeScore + ", subjectiveNum="
				+ subjectiveNum + ", subjectiveScore=" + subjectiveScore + ", pointIds=" + pointIds + ", idpack="
				+ idpack + "]";
	}
    
    

}
