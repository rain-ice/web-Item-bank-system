package shiyan.sys;

import java.util.Calendar;

public class Mydate {
	/**
	 * 计算当前时间
	 * @return 当前时间
	 */
	public static String now() {
		Calendar date = Calendar.getInstance();
		String now =""+date.get(Calendar.YEAR);
		if((date.get(Calendar.MONTH)+1)<10) {
			now+="-0"+(date.get(Calendar.MONTH)+1);
		}else {
			now+="-"+(date.get(Calendar.MONTH)+1);
		}
		if(date.get(Calendar.DAY_OF_MONTH)<10) {
			now+="-0"+date.get(Calendar.DAY_OF_MONTH);
		}else {
			now+="-"+date.get(Calendar.DAY_OF_MONTH);
		}
		return now;
	}

}
