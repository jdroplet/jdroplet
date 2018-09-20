package jdroplet.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ChineseCharUtils {
	// 参考 http://www.cnblogs.com/skyivben/archive/2012/12/22/2829073.html
	
	public enum Source {
		From2500, From3500, From3755;

		public int getValue() {
			return this.ordinal();
		}

		public static Source forValue(int value) {
			return values()[value];
		}
	}

	public static class RandomChineseChar {
		private Character[] source;
		private int sourceLength;
		private java.util.Random random;

		public RandomChineseChar(Source source, int seed) {
			HashSet<Character> tmp = null;
			
			switch (source) {
			case From2500:
				tmp = getChinese2500();
				break;
			case From3500:
				tmp = getChinese3500();
				break;
			case From3755:
				//this.source = (Character[]) GetGB2312String().toArray();
				break;
			// default: //throw new InvalidEnumArgumentException("source",
			// (int)source, source.GetType());
			}
			
			this.source = new Character[tmp.size()];
			tmp.toArray(this.source);
			
			sourceLength = this.source.length;
			random = seed == 0 ? new java.util.Random() : new java.util.Random(
					seed);
		}

		public String getSource() {
			char[] tmp = null;
			
			tmp = new char[this.source.length];
			for(int i=0; i<tmp.length; i++) {
				tmp[i] = this.source[i];
			}
			return new String(tmp);
		}

		public String getString(int count) {
			StringBuilder sb = new StringBuilder(count);
			while (count-- > 0) {
				sb.append(source[random.nextInt(sourceLength)]);
			}
			return sb.toString();
		}

		// 现代汉语常用字中的常用字(共2500个)
		private HashSet<Character> getChinese2500() {
			HashSet<Character> set = getChinese3500();
			String except = null;
			char[] chars = null;

			except = "匕刁丐歹戈夭仑讥冗邓艾夯凸卢叭叽皿凹囚矢乍尔冯玄邦迂邢芋芍吏夷吁吕吆"
					+ "屹廷迄臼仲伦伊肋旭匈凫妆亥汛讳讶讹讼诀弛阱驮驯纫玖玛韧抠扼汞扳抡坎坞抑拟抒芙芜苇芥芯芭杖杉巫"
					+ "杈甫匣轩卤肖吱吠呕呐吟呛吻吭邑囤吮岖牡佑佃伺囱肛肘甸狈鸠彤灸刨庇吝庐闰兑灼沐沛汰沥沦汹沧沪忱"
					+ "诅诈罕屁坠妓姊妒纬玫卦坷坯拓坪坤拄拧拂拙拇拗茉昔苛苫苟苞茁苔枉枢枚枫杭郁矾奈奄殴歧卓昙哎咕呵"
					+ "咙呻咒咆咖帕账贬贮氛秉岳侠侥侣侈卑刽刹肴觅忿瓮肮肪狞庞疟疙疚卒氓炬沽沮泣泞泌沼怔怯宠宛衩祈诡"
					+ "帚屉弧弥陋陌函姆虱叁绅驹绊绎契贰玷玲珊拭拷拱挟垢垛拯荆茸茬荚茵茴荞荠荤荧荔栈柑栅柠枷勃柬砂泵"
					+ "砚鸥轴韭虐昧盹咧昵昭盅勋哆咪哟幽钙钝钠钦钧钮毡氢秕俏俄俐侯徊衍胚胧胎狰饵峦奕咨飒闺闽籽娄烁炫"
					+ "洼柒涎洛恃恍恬恤宦诫诬祠诲屏屎逊陨姚娜蚤骇耘耙秦匿埂捂捍袁捌挫挚捣捅埃耿聂荸莽莱莉莹莺梆栖桦"
					+ "栓桅桩贾酌砸砰砾殉逞哮唠哺剔蚌蚜畔蚣蚪蚓哩圃鸯唁哼唆峭唧峻赂赃钾铆氨秫笆俺赁倔殷耸舀豺豹颁胯"
					+ "胰脐脓逛卿鸵鸳馁凌凄衷郭斋疹紊瓷羔烙浦涡涣涤涧涕涩悍悯窍诺诽袒谆祟恕娩骏琐麸琉琅措捺捶赦埠捻"
					+ "掐掂掖掷掸掺勘聊娶菱菲萎菩萤乾萧萨菇彬梗梧梭曹酝酗厢硅硕奢盔匾颅彪眶晤曼晦冕啡畦趾啃蛆蚯蛉蛀"
					+ "唬啰唾啤啥啸崎逻崔崩婴赊铐铛铝铡铣铭矫秸秽笙笤偎傀躯兜衅徘徙舶舷舵敛翎脯逸凰猖祭烹庶庵痊阎阐"
					+ "眷焊焕鸿涯淑淌淮淆渊淫淳淤淀涮涵惦悴惋寂窒谍谐裆袱祷谒谓谚尉堕隅婉颇绰绷综绽缀巢琳琢琼揍堰揩"
					+ "揽揖彭揣搀搓壹搔葫募蒋蒂韩棱椰焚椎棺榔椭粟棘酣酥硝硫颊雳翘凿棠晰鼎喳遏晾畴跋跛蛔蜒蛤鹃喻啼喧"
					+ "嵌赋赎赐锉锌甥掰氮氯黍筏牍粤逾腌腋腕猩猬惫敦痘痢痪竣翔奠遂焙滞湘渤渺溃溅湃愕惶寓窖窘雇谤犀隘"
					+ "媒媚婿缅缆缔缕骚瑟鹉瑰搪聘斟靴靶蓖蒿蒲蓉楔椿楷榄楞楣酪碘硼碉辐辑频睹睦瞄嗜嗦暇畸跷跺蜈蜗蜕蛹"
					+ "嗅嗡嗤署蜀幌锚锥锨锭锰稚颓筷魁衙腻腮腺鹏肄猿颖煞雏馍馏禀痹廓痴靖誊漓溢溯溶滓溺寞窥窟寝褂裸谬"
					+ "媳嫉缚缤剿赘熬赫蔫摹蔓蔗蔼熙蔚兢榛榕酵碟碴碱碳辕辖雌墅嘁踊蝉嘀幔镀舔熏箍箕箫舆僧孵瘩瘟彰粹漱"
					+ "漩漾慷寡寥谭褐褪隧嫡缨撵撩撮撬擒墩撰鞍蕊蕴樊樟橄敷豌醇磕磅碾憋嘶嘲嘹蝠蝎蝌蝗蝙嘿幢镊镐稽篓膘"
					+ "鲤鲫褒瘪瘤瘫凛澎潭潦澳潘澈澜澄憔懊憎翩褥谴鹤憨履嬉豫缭撼擂擅蕾薛薇擎翰噩橱橙瓢蟥霍霎辙冀踱蹂"
					+ "蟆螃螟噪鹦黔穆篡篷篙篱儒膳鲸瘾瘸糙燎濒憾懈窿缰壕藐檬檐檩檀礁磷瞭瞬瞳瞪曙蹋蟋蟀嚎赡镣魏簇儡徽"
					+ "爵朦臊鳄糜癌懦豁臀藕藤瞻嚣鳍癞瀑襟璧戳攒孽蘑藻鳖蹭蹬簸簿蟹靡癣羹鬓攘蠕巍鳞糯譬霹躏髓蘸镶瓤矗";
			chars = except.toCharArray();

			for(char ch:chars) {
				Iterator<Character> it = set.iterator();
				while (it.hasNext()) {
					if (it.next() == ch) {
						it.remove();
						break;
					}
				}
			}
			return set;
		}

		// 现代汉语常用字(共3500个)
		private HashSet<Character> getChinese3500() {
			HashSet<Character> set = new HashSet<Character>();
			String except = null;
			String source = getGB2312String();
			Character[] chars = null;
			char[] tmp = null;

			tmp = source.toCharArray();
			chars = new Character[tmp.length];

			for (int i = 0; i < chars.length; i++) {  
				chars[i] = tmp[i];
			}  
			set.addAll(Arrays.asList(chars));
	
			except = "皑胺盎敖翱佰稗镑鲍钡苯甭迸毖敝陛卞斌摈炳钵铂箔帛蔡搽诧谗掣郴骋炽踌瞅躇滁"
					+ "搐椽疵茨蹿瘁淬磋傣殆郸惮迪狄翟滇靛凋迭侗恫犊遁掇剁峨娥厄鄂洱珐藩钒酚汾烽氟涪弗釜腑阜讣噶"
					+ "嘎赣皋铬庚龚蛊剐圭癸辊骸氦邯郝菏貉阂涸亨弘瑚桓豢磺簧卉烩姬缉汲蓟伎悸笺缄硷槛饯铰桔睫藉疥"
					+ "靳烬粳痉炯厩咎狙疽咀踞娟撅攫抉浚郡喀咯亢柯侩匡岿奎馈婪阑谰佬镭磊傈涟撂廖霖拎羚陇掳麓潞禄"
					+ "戮挛孪滦纶嘛谩卯酶镁寐醚幂抿牟氖淖妮霓倪拈啮镍涅哦沤啪琶磐耪呸裴抨砒琵毗痞瞥粕莆埔曝沏祁"
					+ "讫扦钎仟堑羌蔷橇鞘沁氰邱酋泅龋颧醛炔榷冉壬妊戎茹孺汝阮鳃莎煽汕缮墒韶邵慑砷娠噬仕孰戍舜朔"
					+ "嗣巳怂擞僳隋绥蓑獭挞酞坍绦锑嚏腆迢眺烃汀酮湍陀哇烷皖韦惟潍渭挝斡钨吾毋戊硒矽嘻烯汐檄襄霄"
					+ "忻惺墟戌嘘眩绚丫焉阉彦佯疡瑶尧噎耶曳铱颐沂彝矣臆裔诣翌荫寅尹臃痈雍恿铀酉釉盂虞俞渝禹峪驭"
					+ "垣苑曰郧匝哉札咋詹辗湛漳瘴肇蛰锗甄砧臻帧峙炙痔诌诛瞩拽篆兹淄孜渍鬃邹纂佐柞";

			//tmp = except.toCharArray();
			//set.retainAll(Arrays.asList(chars));
			tmp = except.toCharArray();
			for(char ch:tmp) {
				Iterator<Character> it = set.iterator();
				while (it.hasNext()) {
					if (it.next() == ch) {
						it.remove();
						break;
					}
				}
			}
			return set;
		}

		// 国标一级字(共3755个): 区:16-55, 位:01-94, 55区最后5位为空位
		private String getGB2312String() {
			List<Byte> list = new ArrayList<Byte>();
			Byte[] tmp = null;
			byte[] bytes = null;
			
			for (int h = 16; h <= 55;h++) {
				for (int l2 = (h == 55) ? 89 : 94, l = 1; l <= l2; l++) {
					list.add((byte) (h + 0xa0));
					list.add((byte) (l + 0xa0));
				}
			}
			tmp = new Byte[list.size()];
			list.toArray(tmp);
			
			bytes = new byte[tmp.length];
			for (int i = 0; i < bytes.length; i++) {  
				bytes[i] = tmp[i];
			} 
			
			try {
				return new String(bytes, "GB2312");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
	}
	
	public static String getChineseChar(int count) {
		ChineseCharUtils.RandomChineseChar rc = new ChineseCharUtils.RandomChineseChar(ChineseCharUtils.Source.From3500, 0);

		return rc.getString(count);
	}
}
