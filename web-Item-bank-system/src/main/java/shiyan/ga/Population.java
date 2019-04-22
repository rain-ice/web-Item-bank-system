package shiyan.ga;

import java.util.List;
import java.util.Random;

import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.model.PaperModel;
import shiyan.table.Question;
import shiyan.table.Rule;

public class Population {
	/**
	 * 试卷合集
	 */
	private PaperModel[] papers;
	
	 /**
     * 知识点权重
     */
    public static final double KP_WEIGHT = 0.20;
    /**
     * 难度权重
     */
    public static final double DIFFCULTY_WEIGHt = 0.80;
	
	/**
	 * 初始种群
	 * 
	 * @param populationSize 种群数量
	 * @param initFlag		  种群初始化标志
	 * @param rule			 规则
	 * @throws Exception 
	 */
	public Population(DbHelper dbHelper,int populationSize,boolean initFlag,Rule rule) throws Exception {
		papers = new PaperModel[populationSize];
		if(initFlag) {
			PaperModel paper;
			Random random = new Random();
			for(int i=0;i<populationSize;i++) {
				paper = new PaperModel();
				paper.setId(i + 1);
				while (paper.getTotalScore() != rule.getTotalMark()) {
					paper.getQuestionList().clear();
					String idString = rule.getPointIds().toString();
					 //填空题
                    if (rule.getCompleteNum() > 0) {
                        generateQuestion(dbHelper,2, random, rule.getCompleteNum(), rule.getCompleteScore(), idString,
                                "填空题数量不够，组卷失败", paper);
                    }
					// 单选题
                    if (rule.getSingleNum() > 0) {
                        generateQuestion(dbHelper,1, random, rule.getSingleNum(), rule.getSingleScore(), idString,
                                "单选题数量不够，组卷失败", paper);
                    }
                   
                    //判断题
                    if (rule.getJudgmentNum() > 0) {
                        generateQuestion(dbHelper,3, random, rule.getJudgmentNum(), rule.getJudgmentScore(), idString,
                                "判断题数量不够，组卷失败", paper);
                    }
                    //问答题
                    if (rule.getSubjectiveNum() > 0) {
                        generateQuestion(dbHelper,4, random, rule.getSubjectiveNum(), rule.getSubjectiveScore(), idString,
                                "问答题数量不够，组卷失败", paper);
                    }
                   System.out.println("试卷"+i+"数量："+paper.getQuestionSize()); 
                   System.out.println("试卷总分："+paper.getTotalScore());
				}
				// 计算试卷知识点覆盖率
                paper.setKpCoverage(rule);
                // 计算试卷适应度
                paper.setAdaptationDegree(rule,KP_WEIGHT,DIFFCULTY_WEIGHt);
                papers[i] = paper;
			}
		}
	}
	/**
	 * 获取试题并存放到paper中
	 * 
	 * @param dbHelper
	 * @param type
	 * @param random
	 * @param qustionNum
	 * @param score
	 * @param idString
	 * @param errorMsg
	 * @param paper
	 * @throws Exception
	 */
	private void generateQuestion (DbHelper dbHelper,int type, Random random, int qustionNum, double score, String idString,
            String errorMsg,PaperModel paper) throws Exception {
		Pagination<Question> pagination = new Pagination<>();
		String where = "where pointid in ("+idString.substring(1, idString.indexOf("]"))+") and type ="+type;
		pagination.appendWhere(where);
		
		pagination =  dbHelper.search(Question.class,pagination);
		List<Question> questions = pagination.getRows(); 
		if (questions.size() < qustionNum) {
			System.out.println(errorMsg);
	            return;
	        }
		Question tmpQuestion;
/*		for (int j = 0; j < qustionNum; j++) {
            int index = random.nextInt(questions.size() - j);
            // 初始化分数
            questions.get(index).setScore(score);
            paper.addQuestion(questions.get(index));
            // 保证不会重复添加试题
            tmpQuestion = questions.get(questions.size() - j - 1);
            questions.set(questions.size() - j - 1,questions.get(index));
            questions.set(index,tmpQuestion);
        }*/
		int k=0,j=0;//k总共的计分点，i代表有几个元素加入了试卷中
		while(k<qustionNum) {
			int index = random.nextInt(questions.size() - j);
			if(questions.get(index).getNumber()>(qustionNum-k)) {
				continue;
			}
			 // 初始化分数
            questions.get(index).setScore(score*questions.get(index).getNumber());
            System.out.println(questions.get(index).getScore());
            paper.addQuestion(questions.get(index));
            k+= questions.get(index).getNumber();
            // 保证不会重复添加试题
            tmpQuestion = questions.get(questions.size() - j - 1);
            questions.set(questions.size() - j - 1,questions.get(index));
            questions.set(index,tmpQuestion);
           
            j++;
		}
		
		
	}
	/**
	 * 获取种群中个的最优个体
	 * @return
	 */
	public PaperModel getFitness() {
		PaperModel paper = papers[0];
		for (int i = 1; i < papers.length; i++) {
            if (paper.getAdaptationDegree() < papers[i].getAdaptationDegree()) {
                paper = papers[i];
            }
        }
        return paper;
	}
	
	public Population(int populationSize) {
        papers = new PaperModel[populationSize];
    }

    /**
     * 获取种群中某个个体
     *
     * @param index
     * @return
     */
    public PaperModel getPaper(int index) {
        return papers[index];
    }
	
    /**
    * 设置种群中某个个体
    *
    * @param index
    * @param paper
    */
   public void setPaper(int index, PaperModel paper) {
       papers[index] = paper;
   }

   /**
    * 返回种群规模
    *
    * @return
    */
   public int getLength() {
       return papers.length;
   }

}
