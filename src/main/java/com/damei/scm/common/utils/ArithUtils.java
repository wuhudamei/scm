package com.damei.scm.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;

/**
 * 数字处理工具类
 **/
public final class ArithUtils {

	private static final int DEF_DIV_SCALE = 10;

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2) {
		return v1 + v2;
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1 被减数
	 * @param v2 减数
	 * @return v1 - v2 的差
	 */
	public static double sub(double v1, double v2) {
		return v1 - v2;
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的乘积
	 */
	public static double mutiply(double v1, double v2) {
		return v1 * v2;
	}

	/**
	 * 提供相对精确的除法运算,当除不净时，保留10小数，小数点10位后的数字四舍五入
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 返回v1/v2的商，最多保留10小数,小数点10位后的数字四舍五入
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供相对精确的除法运算,当除不净时，保留scale位小数，小数点scale位后的数字四舍五入
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示要精确到小数点后几位
	 * @return 返回v1/v2的商，最多保留scale小数,小数点scale位后的数字四舍五入
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal d1 = new BigDecimal(Double.toString(v1));
		BigDecimal d2 = new BigDecimal(Double.toString(v2));
		return d1.divide(d2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 向下取整数 12.9 =>12
	 */
	public static int roundDown(double v) {
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, 0, BigDecimal.ROUND_DOWN).intValue();
	}

	/**
	 * 向上取整数 12.1 =>13
	 */
	public static int roundUp(double v) {
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, 0, BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * 四舍五入取整
	 */
	public static int roundHalfUp(double v) {
		return new BigDecimal(v).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	}

	/**
	 * 返回现金表示形式保留两位小数 如果money是100.53600,返回￥100.54 100 = >￥100.00
	 */
	public static String getCurrency(double money) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.CHINA);
		return formatter.format(money);
	}

	/***
	 * 处理科学计数法表示的金额 <br/>
	 * 100.0 =>100 <br/>
	 * 1.24e2 =>124
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatMoney(double amount) {
		String money = StringUtils.EMPTY;
		int intPart = (int) amount;
		if (amount != intPart) {
			NumberFormat fmt = NumberFormat.getInstance();
			fmt.setMaximumFractionDigits(4);
			money = fmt.format(amount);
			money = money.replace(",", StringUtils.EMPTY);
		} else {
			money = String.valueOf(intPart);
		}
		return money;
	}

	/**
	 * 12.3% => 0.123
	 * 
	 * @param percent 分数 转换成小数
	 * @return
	 */
	public static double percentToDouble(String percent) {
		String floatNum = percent.substring(0, percent.length() - 1);
		double n = NumberUtils.toDouble(floatNum);
		return n = n / 100;
	}

	/**
	 * BigDecimal类型的乘法
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal bigDecimalMultiply(BigDecimal v1, BigDecimal... v2) {
		if (v1 == null || v2 == null) {
			return new BigDecimal(0);
		}
		if (v2.length == 0)
			return v1;
		BigDecimal product = v1;
		for (BigDecimal v : v2) {
			product = product.multiply(v);
		}
		return product;
	}

	/**
	 * BigDecimal类型的除法
	 * 
	 * @param v1 除数
	 * @param v2 被除数
	 * @return
	 */
	public static BigDecimal bigDecimalDivide(BigDecimal v1, BigDecimal v2) {
		if (v1 == null || v2.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		return v1.divide(v2, 4, RoundingMode.HALF_UP);
	}

	/**
	 * BigDecimal类型相加
	 * 
	 * @param vs 不定长数值
	 * @return
	 */
	public static BigDecimal bigDecimalAdd(BigDecimal... vs) {
		if (vs == null || vs.length == 0)
			return BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal v : vs) {
			if (v != null) {
				sum = sum.add(v);
			}
		}
		return sum;
	}

	/**
	 * BigDecimal类型减法
	 * 
	 * @param v 减数
	 * @param vs 被减数
	 * @return 值
	 */
	public static BigDecimal bigDecimalSubtract(BigDecimal v, BigDecimal... vs) {
		if (v == null)
			throw new RuntimeException("减数不能为空！");
		if (vs == null || vs.length == 0)
			return v;
		for (BigDecimal val : vs) {
			if (val != null) {
				v = v.subtract(val);
			}
		}
		return v;
	}

	public static void main(String[] args) {
		//		Map<String, Integer> idMap = new HashMap();
		//		idMap.put("生命期收益", 2);
		//		idMap.put("年化超额收益率", 3);
		//		idMap.put("年化波动率", 6);
		//		idMap.put("最大回撤", 7);
		//		idMap.put("夏普比率", 15);
		//		idMap.put("组合alpha", 4);
		//		idMap.put("Lakeratio", 21);
		//		idMap.put("M-Square", 20);
		//		idMap.put("下半标准差", 9);
		//		idMap.put("索丁诺", 19);
		//		idMap.put("UCR", 5);
		//		idMap.put("DCR", 12);
		//		idMap.put("UCR/DCR", 16);
		//		idMap.put("信息比率", 17);
		//		idMap.put("特雷诺比率", 18);
		//		idMap.put("偏度", 13);
		//		idMap.put("峰度", 14);
		//		idMap.put("Calmar", 22);
		//		idMap.put("组合贝塔", 8);
		//		idMap.put("最大超额回撤", 10);
		//		idMap.put("跟踪误差", 11);

		//		Mean mean = new Mean();
		//		//0.27333, 0.3, 0.501, 0.444, 0.44, 0.34496, 0.33, 0.3, 0.292, 0.667
		//		double[] values = new double[]{-0.0195, -0.0703, 0.0713, -0.1953, 0.0447, 0.0089, -0.0719, 0.2132, 0.0343, 0.0104, -0.1422, 0.0256};
		//
		//		//方差
		//		Variance variance = new Variance();
		//		//标准差
		//		StandardDeviation standardDev = new StandardDeviation();
		//		GeometricMean gMean = new GeometricMean();
		//		System.out.println(mean.evaluate(values));
		//		System.out.println(gMean.evaluate(values));
		//		System.out.println(variance.evaluate(values));
		//		System.out.println(standardDev.evaluate(values));

		// Percentile percentile = new Percentile(); // 百分位数
		//System.out.println("80 percentile value: "   + percentile.evaluate(values, 80.0));
		//Skewness skewness = new Skewness();  //偏度
		// Kurtosis kurtosis = new Kurtosis(); //峰度
		//		System.out.println("skewness: " + skewness.evaluate(values));
		//        System.out.println("kurtosis: " + kurtosis.evaluate(values));

		// PearsonsCorrelation  标的相关性矩阵
		//		PearsonsCorrelation pearCorrel = new PearsonsCorrelation();
		//		double[] xArray = {0.981653033, 0.951405345, 0.585760592, 0.204633844, 0.441370541, 0.267143743, 0.046111999, 0.270196481, 0.473258425, 0.951525521, 0.103819592, 0.799063073, 0.368940295, 0.304776193, 0.348181227, 0.419196272};
		//		double[] yArray = {0.171533567, 0.23846641, 0.141343995, 0.041084831, 0.412515854, 0.874912384, 0.583973873, 0.789356987, 0.838788525, 0.539274246, 0.501678447, 0.181324508, 0.196502231, 0.960280365, 0.130952452, 0.583020813};
		//		double collrectionResult = pearCorrel.correlation(xArray, yArray);
		//		System.out.println(collrectionResult);
		//		Product product = new Product();
		//
		//		List<Integer> allMonthNetValue = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
		//
		//		int idx = 13;
		//		int startIdx = idx - 11;
		//		int endIdx = idx + 1;
		//		List<Integer> rst = allMonthNetValue.subList(startIdx, endIdx);

		//		List<BigDecimal> values = new ArrayList<BigDecimal>();
		//		values.add(BigDecimal.valueOf(-3));
		//		values.add(BigDecimal.valueOf(-2));
		//		values.add(BigDecimal.valueOf(3));
		//		values.add(BigDecimal.valueOf(7));
		//		calcLowerHalfStandardDeviation(values);
	}

	//判断值value 是否在某个范围内
	public static boolean betweenSection(BigDecimal value, BigDecimal lower, BigDecimal upper) {
		if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0)
			return true;
		return false;
	}

	//v1的v2次幂:比如x的n次幂 其中v1,v2都不能为0
	public static BigDecimal pow(double v1, double v2) throws Exception {
		if (v1 == 0 || v2 == 0)
			throw new Exception("底数或次方数不能为零!");

		return new BigDecimal(Math.pow(v1, v2));
	}

	//List<BigDecimal> to double[]
	public static double[] convertBigDecimalToDoubleArry(List<BigDecimal> values) {
		if (Collections3.isEmpty(values))
			return null;
		double[] result = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			result[i] = values.get(i).doubleValue();
		}
		return result;
	}

	//List<BigDecimal> to double[] 只要小于平均值的值
	public static Double calcLowerHalfStandardDeviation(List<BigDecimal> values) {

		//近N年或成立以来的 周标准收益率序列
		double[] weekReturnArray = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			weekReturnArray[i] = values.get(i).doubleValue();
		}
		//算术平均值
		Mean mean = new Mean();
		double weekAvgReturn = mean.evaluate(weekReturnArray);

		List<Double> diffResultList = Lists.newArrayList();
		for (double weekReturn : weekReturnArray) {
			if (weekReturn < weekAvgReturn) {
				diffResultList.add(weekReturn - weekAvgReturn);
			}
		}

		if (diffResultList.isEmpty()) {
			//如果小于平均值的收益率是空，则不计算下半标准差
			return null;
		}

		double[] resultWeekReturnArray = new double[diffResultList.size()];
		for (int j = 0; j < diffResultList.size(); j++) {
			resultWeekReturnArray[j] = diffResultList.get(j);
		}

		//平方和
		SumOfSquares sumOfSqure = new SumOfSquares();
		double sumSqure = sumOfSqure.evaluate(resultWeekReturnArray);
		double lowerHalfStanDeviation = Math.sqrt(sumSqure * 52 / diffResultList.size());
		return lowerHalfStanDeviation;
	}

	/**
	 * BigDecimal类型相加 所有元素的绝对值相加
	 * 
	 * @param vs 不定长数值
	 * @return
	 */
	public static BigDecimal positiveBigDecimalAdd(BigDecimal... vs) {
		if (vs == null || vs.length == 0)
			return BigDecimal.ZERO;
		BigDecimal res = BigDecimal.ZERO;
		for (BigDecimal v : vs) {
			if (v != null) {
				res = res.add(v.abs());
			}
		}
		return res;
	}

	/**
	 * 两个等元素列表的差序列
	 * 
	 * @param xvalues
	 * @param yvalues
	 * @return
	 */
	public static List<BigDecimal> subSequence(List<BigDecimal> xvalues, List<BigDecimal> yvalues) {
		if (Collections3.isEmpty(xvalues) || Collections3.isEmpty(yvalues) || xvalues.size() != yvalues.size())
			return null;

		List<BigDecimal> values = new ArrayList<BigDecimal>(xvalues.size());
		for (int i = 0; i < xvalues.size(); i++) {
			values.add(xvalues.get(i).subtract(yvalues.get(i)));
		}
		return values;
	}
}