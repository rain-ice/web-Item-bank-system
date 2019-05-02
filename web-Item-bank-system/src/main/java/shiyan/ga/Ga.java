package shiyan.ga;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ThisReference;

import shiyan.db.DbHelper;
import shiyan.db.Pagination;
import shiyan.model.PaperModel;
import shiyan.table.Question;
import shiyan.table.Rule;

public class Ga {
	
	/**
     * 变异概率
     */
    private static final double mutationRate = 0.1;
    /**
     * 精英主义
     */
    private static final boolean elitism = true;
    /**
     * 淘汰数组大小
     */
    private static final int tournamentSize = 5;
    

   /**
    * 进化种群
    * 
    * @param dbHelper
    * @param pop
    * @param rule
    * @return
 * @throws Exception 
    */
    public static Population evolvePopulation(DbHelper dbHelper,Population pop,Rule rule) throws Exception {
    	Population newPopulation = new Population(pop.getLength());
    	int elitismOffset;
        // 精英主义
        if (elitism) {
            elitismOffset = 1;
            // 保留上一代最优秀个体
            PaperModel fitness = pop.getFitness();
            fitness.setId(0);
            newPopulation.setPaper(0, fitness);
        }
    	System.out.println("算法执行证明");
     // 种群交叉操作，从当前的种群pop来创建下一代种群newPopulation
        for (int i = elitismOffset; i < newPopulation.getLength(); i++) {
            // 较优选择parent
            PaperModel parent1 = select(pop);
            PaperModel parent2 = select(pop);
            while (parent2.getId() == parent1.getId()) {
                parent2 = select(pop);
            }
            // 交叉
            PaperModel child = crossover(dbHelper,parent1, parent2, rule);
            child.setId(i);
            newPopulation.setPaper(i, child);
        }
     // 种群变异操作
        PaperModel tmpPaper;
        for (int i = elitismOffset; i < newPopulation.getLength(); i++) {
            tmpPaper = newPopulation.getPaper(i);
            mutate(dbHelper,tmpPaper,rule);
            // 计算知识点覆盖率与适应度
            tmpPaper.setKpCoverage(rule);
            tmpPaper.setAdaptationDegree(rule,Population.KP_WEIGHT,Population.DIFFCULTY_WEIGHt);
        }
        
        
        
    	return newPopulation;
    }
    /**
     * 交叉生成下一代
     * @param dbHelper
     * @param paper1
     * @param paper2
     * @param rule
     * @return
     * @throws Exception
     */
    public static PaperModel crossover(DbHelper dbHelper,PaperModel paper1,PaperModel paper2,Rule rule) throws Exception {
    	PaperModel child = new PaperModel();
    	 //获取填空题的数量
        int completeNum = rule.getCompleteNum();
        System.out.println("填空题数量："+completeNum);
        String idString = rule.getPointIds().toString();
        //按照总分来配比
    	int s1 = (int) (Math.random() * rule.getTotalMark());
        int s2 = (int) (Math.random() * rule.getTotalMark());
       
        // paper2的startPos endPos之间的序列，会被遗传到下一代
        int startPos = s1 < s2 ? s1 : s2;
        int endPos = s1 > s2 ? s1 : s2;
        int total=0,i=0;
        while(total<startPos) {
        	child.addQuestion(paper1.getQuestion(i));
        	total+=paper1.getQuestion(i).getScore();
        	if (paper1.getQuestion(i).getType()==2) {
        		completeNum-=paper1.getQuestion(i).getNumber();
        	} 
        	i++;
        }
        //根据两个试卷的差值重新计算i
        i+=(paper2.getQuestionSize()-paper1.getQuestionSize());       
        //防止出现i小于零的情况
        if(i<0) {
        	i=0;
        }
        PaperModel paperTest=paper2;
        while(total<rule.getTotalMark()) {
        	System.out.println("当前试卷成绩："+child.getTotalScore()+"剩余填空数量："+completeNum+"当前i的值："+i);
        	//根据需求切换使用试卷一还是试卷二的
        	if(total>endPos&&paperTest==paper2) {
        		paperTest=paper1;
        		i-=(paper2.getQuestionSize()-paper1.getQuestionSize());
        		 if(i<0) {
        	        	i=0;
        	        }
        		
        	}
        	Question questioni=paperTest.getQuestion(i);
        	if(completeNum>0) {
        		System.out.println("当前填空剩余"+completeNum);
        		//判断数值不存在并且小于填空数量
        		 if (!child.containsQuestion(questioni)&&questioni.getNumber()<=completeNum&&questioni.getType()==2) {
                     child.addQuestion(questioni);
                     total+=questioni.getScore();
                     completeNum-=questioni.getNumber();
                     i++;
                     if(completeNum==0) {
                    	 i=paperTest.getQustionSizeOfComplete();
                     }
                 } else {
                	 Question question = newQuestion(dbHelper, child, 2, idString, completeNum, rule);
                 	
                     child.addQuestion(question);
                     total+= question.getScore();
                     completeNum-= question.getNumber();
                     i++;
                     if(completeNum==0) {
                    	 i=paperTest.getQustionSizeOfComplete();
                     }
                 }
        	}else {
   
        		 if (!child.containsQuestion(questioni)){
        			 child.addQuestion(questioni);
        			 total+=questioni.getScore();
        			 i++;
        		 }else {
        			 Question question = newQuestion(dbHelper, child, questioni.getType(), idString, completeNum, rule);
        
                     child.addQuestion(question);
                     total+= question.getScore();
                     i++;
        		 }
        	}
        }
        System.out.println("最后得到的试卷总分以及题目数量："+child.getTotalScore()+"和"+child.getQuestionSize());
		return child;
    }
    /**
     * 根据当前位置计算对应的题型的分数
     * @param index
     * @param rule
     * @return
     */
    private static double getScoreByIndex(Question question, Rule rule) {
        double score = 0;

   
        if (question.getType()==1) {
        	 // 填空
            score=rule.getSingleScore();
        } else if (question.getType()==2) {
            // 单选
            score = rule.getCompleteScore()*question.getNumber();
        } else if(question.getType()==3){
            // 判断
        	score=rule.getJudgmentScore();
        }else {
        	score=rule.getSubjectiveScore();
        }
        return score;
    }
    /**
     * 返回查询后的问题
     * @param dbHelper
     * @param paper
     * @param type
     * @param idString
     * @param totalNumber
     * @param rule
     * @return
     * @throws Exception
     */
    private static Question newQuestion(DbHelper dbHelper,PaperModel paper,int type,String idString,int totalNumber,Rule rule)throws Exception {
    	String idPackString = paper.getidPackString();
    	 //重新组装数据库查询语句
    	Pagination<Question> pagination = new Pagination<>();
        
        String where = "where pointid in (";
		 where +=idString.substring(1, idString.indexOf("]"))+") and type ="+type;
		 where +=" and id not in ("+idPackString+")";
        pagination.appendWhere(where);
    	//根据条件重新查找问题
        List<Question>  questions = dbHelper.search(Question.class, pagination).getRows();
        int index=(int) (Math.random() * questions.size());
        while(questions.get(index).getNumber()>totalNumber&&type==2) {
       	 index=(int) (Math.random() * questions.size());
        }
        //重新写入分数
        questions.get(index).setScore(getScoreByIndex(questions.get(index),rule));
    	return questions.get(index);
    }
    
    
    /**
     * 选择算子
     *
     * @param population
     */
    private static PaperModel select(Population population) {
        Population pop = new Population(tournamentSize);
        for (int i = 0; i < tournamentSize; i++) {
            pop.setPaper(i, population.getPaper((int) (Math.random() * population.getLength())));
        }
        return pop.getFitness();
    }
    
    /**
     * 突变算子 每个个体的每个基因都有可能突变
     * 
     * @param dbHelper
     * @param paper
     * @throws Exception
     */
    public static void mutate(DbHelper dbHelper,PaperModel paper,Rule rule) throws Exception{
        Question tmpQuestion;
        List<Question> list;
        Question middleQuestion;
        int index;
        for (int i = 0; i < paper.getQuestionSize(); i++) {
            if (Math.random() < mutationRate) {
                // 进行突变，第i道
                tmpQuestion = paper.getQuestion(i);
                int number =  tmpQuestion.getNumber();
                Pagination<Question> pagination = new Pagination<>();
                pagination.appendWhere("where  type = "+tmpQuestion.getType()+" and id not in ("+paper.getidPackString()+")");
                paper.delQuetion(i);
                // 从题库中获取和变异的题目类型
                list = dbHelper.search(Question.class, pagination).getRows();
                
                int j=0;
                while(number>0) {
                	System.out.println("number:"+number);
                	// 随机获取一道
                    index = (int) (Math.random() * (list.size()-j));
                    //查看数量是否超标
                    System.out.println("随机数的值："+index);
                    while(list.get(index).getNumber()>number) {
                    	index = (int) (Math.random() * list.size()-j);
                    }
                    // 设置分数
                    list.get(index).setScore(tmpQuestion.getScore());
                    paper.insertQuestion(i, list.get(index));
                    // 保证不会重复添加试题
                    middleQuestion = list.get(list.size() - j - 1);
                    list.set(list.size() - j - 1,list.get(index));
                    list.set(index,middleQuestion);
                    
                    number-=list.get(index).getNumber();
                    j++;
                }
                    
                }
            }
        }

}
