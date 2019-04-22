package shiyan.db;



import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import shiyan.ga.Ga;
import shiyan.ga.Population;
import shiyan.model.PaperModel;
import shiyan.table.Rule;

public class Test {

	public static void main(String[] args) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		DbHelper dbHelper = new DbHelper(jdbcTemplate);
		PaperModel resultPaper = null;
        // 迭代计数器
        int count = 0;
        int runCount = 100;
        // 适应度期望值
        double expand = 0.98;
        // 可自己初始化组卷规则rule
        Rule rule = new Rule();
        rule.setTotalMark(100);
        rule.setCompleteNum(5);
        rule.setCompleteScore(10);
        rule.setJudgmentNum(2);
        rule.setJudgmentScore(10);
        rule.setLevel(2);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        rule.setPointIds(list);
        rule.setSingleNum(3);
        rule.setSingleScore(10);
        if (rule != null) {
            // 初始化种群
            Population population = new Population(dbHelper,20, true, rule);
            System.out.println("初次适应度  " + population.getFitness().getAdaptationDegree());
            while (count < runCount && population.getFitness().getAdaptationDegree() < expand) {
                count++;
                population = Ga.evolvePopulation(dbHelper,population, rule);
                System.out.println("第 " + count + " 次进化，适应度为： " + population.getFitness().getAdaptationDegree());
            }
            System.out.println("进化次数： " + count);
            System.out.println(population.getFitness().getAdaptationDegree());
            resultPaper = population.getFitness();
        }
        System.out.println(resultPaper);

	}

}
