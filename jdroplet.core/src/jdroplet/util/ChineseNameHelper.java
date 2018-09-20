package jdroplet.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChineseNameHelper {
	private static String FIRST_NAMES[] = new String[] { "白", "毕", "卞", "蔡",
			"曹", "岑", "常", "车", "陈", "成", "程", "池", "邓", "丁", "范", "方", "樊",
			"费", "冯", "符", "傅", "甘", "高", "葛", "龚", "古", "关", "郭", "韩", "何",
			"贺", "洪", "侯", "胡", "华", "黄", "霍", "姬", "简", "江", "姜", "蒋", "金",
			"康", "柯", "孔", "赖", "郎", "乐", "雷", "黎", "李", "连", "廉", "梁", "廖",
			"林", "凌", "刘", "柳", "龙", "卢", "鲁", "陆", "路", "吕", "罗", "骆", "马",
			"梅", "孟", "莫", "母", "穆", "倪", "宁", "欧", "区", "潘", "彭", "蒲", "皮",
			"齐", "戚", "钱", "强", "秦", "丘", "邱", "饶", "任", "沈", "盛", "施", "石",
			"时", "史", "司徒", "苏", "孙", "谭", "汤", "唐", "陶", "田", "童", "涂", "王",
			"危", "韦", "卫", "魏", "温", "文", "翁", "巫", "邬", "吴", "伍", "武", "席",
			"夏", "萧", "谢", "辛", "邢", "徐", "许", "薛", "严", "颜", "杨", "叶", "易",
			"殷", "尤", "于", "余", "俞", "虞", "元", "袁", "岳", "云", "曾", "詹", "张",
			"章", "赵", "郑", "钟", "周", "邹", "朱", "褚", "庄", "卓", "欧阳", "太史", "端木",
			"上官", "司马", "东方", "独孤", "南宫", "万俟", "闻人", "夏侯", "诸葛", "尉迟", "公羊",
			"赫连", "澹台", "皇甫", "宗政", "濮阳", "公冶", "太叔", "申屠", "公孙", "慕容", "仲孙",
			"钟离", "长孙", "宇文", "司徒", "鲜于", "司空", "闾丘", "子车", "亓官", "司寇", "巫马",
			"公西", "颛孙", "壤驷", "公良", "漆雕", "乐正", "宰父", "谷梁", "拓跋", "夹谷", "轩辕",
			"令狐", "段干", "百里", "呼延", "东郭", "南门", "羊舌", "微生", "公户", "公玉", "公仪",
			"梁丘", "公仲", "公上", "公门", "公山", "公坚", "左丘", "公伯", "西门", "公祖", "第五",
			"公乘", "贯丘", "公皙", "南荣", "东里", "东宫", "仲长", "子书", "子桑", "即墨", "达奚",
			"褚师", "吴铭" };

	private static String LAST_NAME_MALE = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";

	private static String LAST_NAME_FEMA = "秀语娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊嫣兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝鱼晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽墨";

	public static String getName(boolean gender) {
		List<String> names = null;

		names = getNames(gender, 1);
		return names.get(0);
	}

	public static List<String> getNames(boolean gender, int count) {
		Integer[] idxfn = null;
		Integer[] idxln1 = null;
		Integer[] idxln2 = null;
		List<String> names = null;
		String last_name = null;
		int nameLength = 0;

		last_name = gender ? LAST_NAME_MALE : LAST_NAME_FEMA;

		idxfn = generateNonRepeatArray(count);
		idxln1 = generateNonRepeatArray(count);
		idxln2 = generateNonRepeatArray(count);

		nameLength = last_name.length();
		names = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			int v1 = idxfn[i];
			int v2 = idxln1[i];
			int v3 = idxln2[i];

			if (v1 >= FIRST_NAMES.length) {
				int j = idxfn[i] / FIRST_NAMES.length;
				v1 = idxfn[i] - FIRST_NAMES.length * j;
			}
			if (v2 >= nameLength) {
				int j = idxln1[i] / nameLength;
				v2 = idxln1[i] - nameLength * j;
			}
			if (v3 >= nameLength) {
				int j = idxln2[i] / nameLength;
				v3 = idxln2[i] - nameLength * j;
			}

			// if (v1 < 0 || v2 < 0 || v3 < 0) {
			// }

			// if (v1 >= FIRST_NAMES.length || v2 >= last_name.length()
			// || v3 >= last_name.length()) {
			// }

			names.add(MessageFormat.format("{0}{1}{2}", FIRST_NAMES[v1],
					last_name.substring(v2, v2 + 1),
					last_name.substring(v3, v3 + 1)));

		}
		return names;
	}

	private static Integer[] generateNonRepeatArray(int count) {
		int item = 0;
		List<Integer> temp = null;
		Integer datas[] = null;
		Random ran = null;

		temp = new ArrayList<Integer>();
		ran = new Random();
		while (temp.size() < count) {
			item = ran.nextInt(10000);
			if (!temp.contains(item))
				temp.add(item);
		}
		datas = new Integer[temp.size()];
		temp.toArray(datas);
		return datas;
	}
}
