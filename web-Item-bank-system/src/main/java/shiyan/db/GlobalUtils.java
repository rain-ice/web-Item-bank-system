package shiyan.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;



/**
 * 静态的一些方法
 * 一定程度上取代工具包里面的方法
 * 尤其是一些需要传输给上层应用的对象，以免上层应用需要额外的jar包
 * @author IcekingT420
 *
 */
public class GlobalUtils {
	/**
	 * long型转时间格式
	 * @param time
	 * @param format
	 * @return
	 */
	public static String timeToDateString(long time,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(new Date(time));
	}
	
	public static Date timeStringToDate(String timeString,String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(timeString);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 多种时间格式，只要一种可以parse即可
	 * @param timeString
	 * @param formats
	 * @return
	 */
	public static Date timeStringToDate2(String timeString,String... formats){
		if(formats == null || formats.length == 0)
			return null;
		Date date = null;
		for(String format: formats){
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				date = dateFormat.parse(timeString);
				if(date != null)
					break;
			} catch (Exception e) {
				date = null;
			}
		}
		return date;
	}
	
	/**
	 * byte转为int
	 * @param b
	 * @return
	 */
	public static int b2i(byte b) {
		if (b >= 0)
			return b;
		else
			return b + 256;
	}
	
	/**
	 * 取出第index个字节
	 * @param l
	 * @param index
	 * @return
	 */
	public static byte longToByteArray(long l, int index) {
		return (byte) ((l >> index * 8) & 0xff);
	}
	
	/**
	 * 从一个整数中得到字节数组
	 * @param l 整数
	 * @param len 字节数组的长度，长度必须小于等于8
	 * @return
	 */
	public static byte[] getArrayFromLong(long l,int len) {
		byte[] array = new byte[len];
		for(int i=0;i<len;i++){
			array[i] = longToByteArray(l, len-i-1);
		}
		return array;
	}
	
	/**
	 * 获取二进制位的第几位数值
	 * @param data
	 * @param pos
	 * @return
	 */
	public static int movePos(long l, int pos) {
		long i = l >> pos;
		return (int)(i & 1);
	}
	
	/**
	 * byte转为16进制字符
	 * @param b
	 * @return
	 */
	public static String byteToHex(byte b){
		String str = Long.toHexString(b);
		if (str.length() < 2)
			str = "0" + str;
		if (str.length() > 2)
			str = str.substring(str.length() - 2, str.length());
		return str;
	}
	
	/**
	 * byte数组变为16进制字符串
	 * @param array
	 * @return
	 */
	public static String byteArrayToHex(byte[] array){
		String hex = "";
		if(array != null && array.length>0){
			for(byte b: array){
				hex += byteToHex(b);
			}
		}
		return hex;
	}
	
	/**
	 * 16进制转byte
	 * @param hex
	 * @return
	 */
	public static byte hexToByte(String hex){
		return (byte) Long.parseLong(hex, 16);
	}
	
	/**
	 * 16进制字符串变为数组，2个字符表示一个16进制，中间无间隔符号
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByteArray(String hex){
		if (hex==null || hex.length() % 2 != 0) {
			return null;
		}
		byte[] od = new byte[hex.length() / 2];
		int start = 0;
		for (int i = 0; i < od.length; i++) {
			try {
				String tmp = hex.substring(start, start + 2);
				od[i] = hexToByte(tmp);
			} catch (Exception e) {
				return null;
			}
			start += 2;
		}
		return od;
	}
	
	/**
	 * 中间有间隔符号的变为数组
	 * @param hex
	 * @param sep
	 * @return
	 */
	public static byte[] hexToByteArray(String hex,String sep){
		String str = hex.replace(sep, "");
		return hexToByteArray(str);
	}
	
	public static boolean isEmpty(List<?> list){
		if(list == null || list.size() == 0)
			return true;
		return false;
	}
	
	public static boolean isEmpty(String str){
		if(str == null || str.equals(""))
			return true;
		return false;
	}
	
	public static boolean isLong(String str){
		try {
			Long.parseLong(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获得进程的PID
	 * 可以用于后续的内存检测
	 * @return
	 */
	public static int getPID(){
		try {
			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			String name = runtime.getName();
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
	/**
	 * 字符加密为md5
	 * @param originString
	 * @return
	 */
	public static String md5Encode(String originString) {
		byte digest[] = (byte[]) null;
		try {
			MessageDigest alg = MessageDigest.getInstance("MD5");
			alg.update(originString.getBytes());
			digest = alg.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return byteArrayToHex(digest);
	}
	
	
	
	/**
	 * 线程休眠
	 * @param mm
	 */
	public static void sleepIt(long mm){
		try {
			Thread.sleep(mm);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 获得根目录 user.dir
	 */
	private static File userDir;

	public static File getProjectDir() {
		if(userDir == null){
			userDir = new File(System.getProperty("user.dir"));
		}
		return userDir;
	}
	
	/**
	 * 用户目录，项目的目录，可以作为外部设置进去的
	 * @param userDir
	 */
	public static void setUserDir(File userDir) {
		GlobalUtils.userDir = userDir;
	}

	/**
	 * 获得工程根目录的路径，字符形式的
	 * 如果在windows平台下，\\替换为/
	 * @return
	 */
	public static String getProjectPath(){
		String path = getProjectDir().getPath();
		path = replace(path, "\\", "/");
		return path;
	}
	
	/**
	 * 获得java jre的目录
	 * java.home
	 */
	private static String jreHome;

	public static String getJreHome() {
		if(jreHome == null){
			jreHome = System.getProperty("java.home");
			jreHome = replace(jreHome, "\\", "/");
		}
		return jreHome;
	}
	
	/**
	 * 单行文本的替换
	 * @param source
	 * @param oldStr
	 * @param newStr
	 * @return
	 */
	public static String replace(String source,String oldStr,String newStr){
		//需要替换的旧字符串，必须要有的
		if(oldStr == null || oldStr.equals(""))
			return source;
		
		//循环处理，直到没有 oldStr 为止
		int idx = -1;
		String dest = new String(source);
		while((idx = dest.indexOf(oldStr)) != -1){
			String bf = dest.substring(0, idx);
			String aft = dest.substring(idx+oldStr.length());
			dest = bf+(newStr!=null?newStr:"")+aft;
		}
		
		return dest;
	}
	
	
	
	
	
	/**
	 * 二进制的字符转换为一个byte
	 * @param binaryStr
	 * @return
	 */
	public static byte parseBinary(String binaryStr){
		return Byte.parseByte(binaryStr, 2);
	}
	
	public static int parseBinaryInt(String binaryStr){
		return b2i(parseBinary(binaryStr));
	}
	
	/**
	 * 十进制转二进制，如果长度不足len，则前面补0
	 * @param num
	 * @param strLen
	 * @return
	 */
	public static String toBinary(int num,int strLen){
		String str = Integer.toBinaryString(num);
		if(str.length()<strLen){
			//需要补0的数量
			int pad0Num = strLen-str.length();
			for(int i=0;i<pad0Num;i++){
				str = "0"+str;
			}
		}
		return str;
	}
	
	/**
	 * 从一个整数中截取出一段二进制字符
	 * @param l
	 * @param startPos
	 * @param endPos
	 * @return
	 */
	public static String curBinaryString(long l,int startPos,int endPos){
		if(startPos == endPos){
			return String.valueOf(movePos(l,startPos));
		}else{
			String tmp = "";
			int min = 0;
			int max = 0;
			if(startPos > endPos){
				min = endPos;
				max = startPos;
			}else{
				min = startPos;
				max = endPos;
			}
			for(int i=max;i>=min;i--){
				tmp += String.valueOf(movePos(l,i));
			}
			return tmp;
		}
	}
	
	/**
	 * 由于考虑DES加密的明文必须为8的倍数，
	 * 因此使用0x00补足
	 * @param array
	 * @return
	 */
	public static byte[] padding8(byte[] array){
		return paddingX(array, 8);
	}
	
	/**
	 * 给AES使用的，明文必须为16的倍数，补足
	 * @param array
	 * @return
	 */
	public static byte[] padding16(byte[] array){
		return paddingX(array,16);
	}
	
	/**
	 * 明文补足到x的倍数
	 * @param array
	 * @param x
	 * @return
	 */
	public static byte[] paddingX(byte[] array,int x){
		//能被8整除，直接返回
		if(array.length % x == 0)
			return array;
		//需要补足的位数
		int padnum = x - array.length%x;
		//构建新的数组
		byte[] reArray = new byte[array.length+padnum];
		//拷贝原数据到新数组，后面补足位自动为0x00
		for(int i=0;i<array.length;i++){
			reArray[i] = array[i];
		}
		return reArray;
	}
	
	/**
	 * 从补足后的数组中去掉补足的位数，得到原始数组
	 * @param padArray
	 * @param padNum
	 * @return
	 */
	public static byte[] recoverPadding8(byte[] padArray,int padNum){
		//异常情况
		if(padNum<=0 || padArray.length<= padNum)
			return padArray;
		byte[] array = new byte[padArray.length-padNum];
		for(int i=0;i<array.length;i++){
			array[i] = padArray[i];
		}
		return array;
	}
	
	/**
	 * 判断两个字节数组是否完全相等
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isTwoArrayEquals(byte[] a,byte[] b){
		if(a == null || b == null)
			return false;
		if(a.length != b.length)
			return false;
		for(int i=0;i<a.length;i++){
			if(a[i] != b[i])
				return false;
		}
		return true;
	}
	
	/**
	 * 将一个字节数组的顺序进行反转，适用于Big Endian 和 Little Endian之间的转换
	 * java默认的顺序是Big Endian，而C默认的是Little Endian
	 * @param array
	 * @return
	 */
	public static byte[] reverseArray(byte[] array){
		if(array == null || array.length==0)
			return array;
		int len = array.length;
		byte[] reArray = new byte[len];
		for(int i=0;i<len;i++){
			reArray[i] = array[len-1-i];
		}
		return reArray;
	}
	
	/**
	 * 从一个数组中拿出其中的一段
	 * @param array
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] gnrNewArray(byte[] array,int start,int length){
		if(start>=array.length)
			return null;
		int curStart = start<0?0:start;
		ByteBuffer buffer = new ByteBuffer();
		for(int i=0;i<length;i++){
			int index = curStart+i;
			if(index == array.length)
				break;
			buffer.add(array[index]);
		}
		return buffer.toArrayAndClose();
	}
	
	/**
	 * 对数组求和，目前主要用于判断一个数组是否全部为0
	 * @param array
	 * @return
	 */
	public static int sumOfArray(byte[] array){
		int sum = 0;
		for(int i=0;i<array.length;i++){
			sum += b2i(array[i]);
		}
		return sum;
	}
	
	/**
	 * 对象序列化方法
	 * @param obj
	 * @return
	 */
	public static byte[] serializeToBytes(Serializable obj){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] array = baos.toByteArray();
			oos.close();
			baos.close();
			return array;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 把一个byte[]反序列化为一个实现Serializable接口的对象
	 * @param array
	 * @return
	 */
	public static Serializable deSerializeToObject(byte[] array){
		Serializable obj = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(array);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object tmp = ois.readObject();
			if(tmp != null && (tmp instanceof Serializable)){
				obj = (Serializable)tmp;
			}
			ois.close();
			bais.close();
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
		}
		return obj;
	}
	
	/**
	 * 将一个可以序列化的对象转变成16进制的数组字符
	 * @param obj
	 * @return
	 */
	public static String objectToHex(Serializable obj){
		byte[] array = serializeToBytes(obj);
		if(array == null)
			return null;
		return byteArrayToHex(array);
	}
	
	/**
	 * 将一个16进制字符转换为对象
	 * @param hexsString
	 * @return
	 */
	public static Serializable hexToObject(String hexsString){
		byte[] array = hexToByteArray(hexsString);
		if(array == null)
			return null;
		return deSerializeToObject(array);
	}
	
	/**
	 * 数组转base64
	 * @param array
	 * @return
	 */
	public static String arrayToBase64(byte[] array){
		return Base64.getEncoder().encodeToString(array);
	}
	
	/**
	 * base64加密的字符转字节数组
	 * @param base64
	 * @return
	 */
	public static byte[] base64ToArray(String base64){
		return Base64.getDecoder().decode(base64);
	}
	
	/**
	 * 对象转base64
	 * @param obj
	 * @return
	 */
	public static String objectToBase64(Serializable obj){
		byte[] array = serializeToBytes(obj);
		if(array == null)
			return null;
		return arrayToBase64(array);
	}
	
	/**
	 * base64的字符转对象
	 * @param base64
	 * @return
	 */
	public static Serializable base64ToObject(String base64){
		byte[] array = base64ToArray(base64);
		if(array == null)
			return null;
		return deSerializeToObject(array);
	}
	
	
	/**
	 * 从字节数组获得long
	 * @param array
	 * @return
	 */
	public static long getLongFromArray(byte[] array){
		long l = 0;
		int len = array.length;
		for(int i=0;i<len;i++){
			l = l + (long)(b2i(array[i])*Math.pow(256, len-i-1));
		}
		return l;
	}
	
	/**
	 * 取出整数int
	 * @param array
	 * @return
	 */
	public static int getIntFromArray(byte[] array){
		return (int)getLongFromArray(array);
	}
	
	/**
	 * 取出整数 short
	 * @param array
	 * @return
	 */
	public static short getShortFromArray(byte[] array){
		return (short)getLongFromArray(array);
	}
	
	
	/**
	 * 有间隔符号的
	 * @param array
	 * @param sep
	 * @return
	 */
	public static String byteArrayToHex(byte[] array,String sep){
		String hex = "";
		if(array != null && array.length>0){
			for(int i=0;i<array.length;i++){
				if(i>0)
					hex += sep;
				hex += byteToHex(array[i]);
			}
		}
		return hex;
	}
	
	
	
	public static boolean isEmpty(byte[] array){
		if(array == null || array.length==0)
			return true;
		return false;
	}
	
	/**
	 * 获得本机的IP地址
	 */
	private static String localIPV4 = null;
	
	public static String getLocalIP() {
		if(localIPV4 == null){
			try {
				localIPV4 = Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				localIPV4 = "127.0.0.1";
			}
		}
		return localIPV4;
	}
	
	
	/**
	 * 控制线程的wait和notify
	 */
	private static final String ThreadWaitResource = "ThreadWaitResource";
	/**
	 * 让执行该方法的线程处于等待状态
	 * 直到被其它线程唤醒
	 */
	public static void waitCurThread(){
		waitCurThread(ThreadWaitResource);
	}
	public static void waitCurThread(final Object lock){
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 唤醒其它等待的线程
	 */
	public static void notifyOtherThread(){
		notifyOtherThread(ThreadWaitResource);
	}
	public static void notifyOtherThread(final Object lock){
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	
	/**
	 * json数据的格式化
	 */
	/**
	 * json格式化的间隔符，单位缩进字符串
	 */
	private static String SPACE = "   ";
	
	/**
	 * 对json字符串的格式化
	 * @param json
	 * @return
	 */
	public static String formatJson(String json) {
		StringBuffer result = new StringBuffer();

		int length = json.length();
		int number = 0;
		char key = 0;

		// 遍历输入字符串。
		for (int i = 0; i < length; i++) {
			// 1、获取当前字符。
			key = json.charAt(i);

			// 2、如果当前字符是前方括号、前花括号做如下处理：
			if ((key == '[') || (key == '{')) {
				// （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(formatJsonIndent(number));
				}

				// （2）打印：当前字符。
				result.append(key);

				// （3）前方括号、前花括号，的后面必须换行。打印：换行。
				result.append('\n');

				// （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				number++;
				result.append(formatJsonIndent(number));

				// （5）进行下一次循环。
				continue;
			}

			// 3、如果当前字符是后方括号、后花括号做如下处理：
			if ((key == ']') || (key == '}')) {
				// （1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append('\n');

				// （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				number--;
				result.append(formatJsonIndent(number));

				// （3）打印：当前字符。
				result.append(key);

				// （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}

				// （5）继续下一次循环。
				continue;
			}

			// 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if ((key == ',')) {
				result.append(key);
				result.append('\n');
				result.append(formatJsonIndent(number));
				continue;
			}

			// 5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}
	
	private static String formatJsonIndent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(SPACE);
		}
		return result.toString();
	}
	
	//=========及格Helper中通用的一些方法
	
	/**
	 * 简单java中对象中包含的数据类型<br/>
	 */
	public static final List<String> simpleDataTypes = Arrays.asList(
			"String", 
			"int", "Integer", 
			"long", "Long",
			"double", "Double", 
			"boolean", "Boolean", 
			"float", "Float", 
			"short", "Short", 
			"byte", "Byte");
	
	/**
	 * 首字母大写，用于组装getter和setter方法名<br/>
	 * 
	 * @param str
	 * @return
	 */
	private static String firstUpper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * 根据属性名称，返回getter或者setter方法的名称
	 * @param fieldName
	 * @param prefix
	 * @return
	 */
	private static String nameOfGetterOrSetter(String fieldName,String prefix){
		//第一个字符
		char c0 = fieldName.charAt(0);
		
		//第一个字符，如果已经是大写了，那么就直接返回get+fieldName
		if(Character.isUpperCase(c0)){
			return prefix+fieldName;
		}
		
		//属性名长度只有1，特殊情况，一般不会发生
		if(fieldName.length()==1){
			c0 = Character.toUpperCase(c0);
			return prefix+new String(new char[]{c0});
		}
		
		
		//第二个字符
		char c1 = fieldName.charAt(1);
		//如果第一个字符小写，第二个字符大写，特殊情况
		//此时，直接返回：get+fieldName, 即不需要把第一个字母大写了
		if(Character.isLowerCase(c0) && Character.isUpperCase(c1)){
			return prefix+fieldName;
		}
		
		//其它情况，第一个字符大写后返回
		return prefix+firstUpper(fieldName);
	}
	
	/**
	 * 获得一个属性的getter方法
	 * @param fieldName
	 * @return
	 */
	public static String nameOfGetter(String fieldName){
		return nameOfGetterOrSetter(fieldName, "get");
	}
	
	/**
	 * 获得一个属性的setter方法
	 * @param fieldName
	 * @return
	 */
	public static String nameOfSetter(String fieldName){
		return nameOfGetterOrSetter(fieldName, "set");
	}
	
	/**
	 * 转换操作，根据java反射机制进行 <br/>
	 * 此处仅仅对rs指向的一行记录进行转换<br/>
	 * @param rs
	 * @param obj
	 */
	public static <T> void oneRsToObj(ResultSet rs, T obj) throws Exception{
			// 找出cls中的全部私有属性
			Field[] allFields = obj.getClass().getDeclaredFields();
			for (Field fd : allFields) {
				if (fd.getModifiers() != 2)
					continue;
				// 根据类型，组装ResultSet中的方法名字
				String mtdNameOfRs = GlobalUtils.nameOfGetter(fd.getType().getSimpleName());
				// 判断ResultSet是否有这个方法，如果没有，则忽略
				Method mtdOfRs = null;
				try {
					mtdOfRs = ResultSet.class.getMethod(mtdNameOfRs, String.class);
				} catch (Exception e) {
					continue;
				}
				// 根据名字，获得obj中的方法
				String mtdNameOfObj = GlobalUtils.nameOfSetter(fd.getName());
				// 判断obj中是否有这个方法
				Method mtdOfObj = obj.getClass().getMethod(mtdNameOfObj, fd.getType());
				// 进行方法的调用
				Object rst = mtdOfRs.invoke(rs, fd.getName());
				mtdOfObj.invoke(obj, rst);
			}
	}
	
}
