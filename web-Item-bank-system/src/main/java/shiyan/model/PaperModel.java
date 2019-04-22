package shiyan.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import shiyan.table.Question;
import shiyan.table.Rule;

public class PaperModel {
	/**
     * 个体id
     */
    private int id;
    /**
     * 试卷名称
     */
    private String name;
    
    /**
     * 适应度
     */
    private double adaptationDegree = 0.00;
    /**
     * 知识点覆盖率
     */
    private double kPCoverage = 0.00;
    /**
     * 试卷总分
     */
    private double totalScore = 0.00;
    /**
     * 试卷难度系数
     */
    private double level = 0.00;
    /**
     * 预计的总分
     */
    private double totalMark=0.00;
    /**
     * 个体包含的试题集合
     */
    private List<Question> questionList = new ArrayList<Question>();
    
    
    
    
    /**
     * 方便交叉算子时生成空白试卷导入题型
     * @param size
     */
    public PaperModel(int size) {
    	for(int i = 0;i<size;i++) {
    		questionList.add(null);
    	}
    }
    
    public PaperModel() {
    	super();
    }
    /**
     * 计算试卷总分
     * @return
     */
    public double getTotalScore() {
    		double total = 0;
    		for(Question question:questionList) {
    			total +=question.getScore();
    		}
    		totalScore = total;

    		return total;
    }
    
    /**
     * 计算试卷个体难度系数 ：（每题难度*分数求）/总分
     * @return
     */
    public double getLevel() {
    	 if (level == 0) {
    		 double _level = 0;
    		 for (Question question : questionList) {
    			 _level += question.getScore() * question.getGlevel();
             }
    		 level = _level / getTotalScore();
    	 }
    	 return level;
    }
    
    /**
     * 获取题目数量
     * 
     * @return
     */
    public int getQuestionSize() {
        return questionList.size();
    }
    /**
     * 计算填空题的截止题数
     * @return
     */
    public int getQustionSizeOfComplete() {
    	int i=0;
    	for(Question question:questionList) {
    		if(question.getType()!=2) {
    			break;
    		}
    		i++;
    	}
    	return i;
    }
    
    /**
     * 计算知识点覆盖率 公式为：个体包含的知识点/期望包含的知识点
     *
     * @param rule
     */
    public void setKpCoverage(Rule rule) {
        if (kPCoverage == 0) {
            Set<String> result = new HashSet<String>();
            result.addAll(rule.getPointIds());
            Set<String> another = questionList.stream().map(questionBean -> String.valueOf(questionBean.getPointid())).collect(Collectors.toSet());
            // 交集操作
            result.retainAll(another);
            kPCoverage = result.size() / rule.getPointIds().size();
        }
    }
    
    
    /**
     * 计算个体适应度 公式为：f=1-(1-M/N)*f1-|EP-P|*f2
     * 其中M/N为知识点覆盖率，EP为期望难度系数，P为种群个体难度系数，f1为知识点分布的权重
     * ，f2为难度系数所占权重。当f1=0时退化为只限制试题难度系数，当f2=0时退化为只限制知识点分布
     *
     * @param rule 组卷规则
     * @param f1   知识点分布的权重
     * @param f2   难度系数的权重
     */
    public void setAdaptationDegree(Rule rule, double f1, double f2) {
        if (adaptationDegree == 0) {
            adaptationDegree = 1 - (1 - getkPCoverage()) * f1 - Math.abs(rule.getLevel() - getLevel()) * f2;
        }
    }
    /**
     * 判断试卷中手否含有相同问题
     * 
     * @param question
     * @return
     */
    public boolean containsQuestion(Question question) {
        if (question == null) {
            for (int i = 0; i < questionList.size(); i++) {
                if (questionList.get(i) == null) {
                    return true;
                }
            }
        } else {
            for (Question aQuestionList : questionList) {
                if (aQuestionList != null) {
                    if (aQuestionList.equals(question)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 返回当前所拥有的的试题ID集合，以字符串的形式拼接
     * @return
     */
    public String getidPackString() {
    	String idPackString="";
    	for(int i=0;i<questionList.size();i++) {
    		idPackString +=questionList.get(i).getId()+",";
    	}
    	if(!idPackString.equals("")) {
       	 idPackString = idPackString.substring(0,idPackString.length() - 1);
       }
    	return idPackString;
    }

    /**
     * 增加问题
     *
     * @param question
     */
    public void insertQuestion(int index, Question question) {
        this.questionList.add(index, question);
        this.totalScore = 0;
        this.adaptationDegree = 0;
        this.level = 0;
        this.kPCoverage = 0;
    }
    
    public void delQuetion(int index) {
    	this.questionList.remove(index);
    	this.totalScore = 0;
        this.adaptationDegree = 0;
        this.level = 0;
        this.kPCoverage = 0;
    }

    public void addQuestion(Question question) {
        this.questionList.add(question);
        this.totalScore = 0;
        this.adaptationDegree = 0;
        this.level = 0;
        this.kPCoverage = 0;
    }

    public Question getQuestion(int index) {
        return questionList.get(index);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getkPCoverage() {
        return kPCoverage;
    }

    public double getAdaptationDegree() {
        return adaptationDegree;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }
    
    
    

}
