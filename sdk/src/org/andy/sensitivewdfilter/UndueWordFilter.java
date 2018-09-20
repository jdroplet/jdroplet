package org.andy.sensitivewdfilter;

import jdroplet.core.SystemConfig;
import jdroplet.util.TextUtils;
import org.andy.sensitivewdfilter.util.BCConvert;

import java.io.*;
import java.util.*;

/**
 * 创建时间：2016年8月30日 下午3:01:12
 * <p>
 * 思路： 创建一个FilterSet，枚举了0~65535的所有char是否是某个敏感词开头的状态
 * <p>
 * 判断是否是 敏感词开头 | | 是 不是 获取头节点 OK--下一个字 然后逐级遍历，DFA算法
 *
 * @author andy
 * @version 2.2
 */
public class UndueWordFilter {

    public class FilterContent {
        private List<String> words;
        private String content;
        private String maskContent;

        public FilterContent() {
            words = new ArrayList<>();
        }

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMaskContent() {
            return maskContent;
        }

        public void setMaskContent(String maskContent) {
            this.maskContent = maskContent;
        }
    }

    private final FilterSet sSet = new FilterSet(); // 存储首字
    private final Map<Integer, WordNode> sNodes = new HashMap<Integer, WordNode>(1024, 1); // 存储节点
    private final Set<Integer> sStopwdSet = new HashSet<>(); // 停顿词
    private final char MASKER = '*'; // 敏感词过滤替换

    public UndueWordFilter(int level) {
        List<String> words = null;
        FileInputStream fis = null;
        String filePath = null;

        filePath = new StringBuilder(64)
                .append(SystemConfig.getServerPath())
                .append(SystemConfig.getConfigPath())
                .append("/undue_word_l" + level + ".txt")
                .toString();

        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            return;
        }
        words = readWordFromFile(fis);
        addSensitiveWord(words);


        filePath = new StringBuilder(64)
                .append(SystemConfig.getServerPath())
                .append(SystemConfig.getConfigPath())
                .append("/stopwd.txt")
                .toString();

        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            return;
        }
        words = readWordFromFile(fis);
        addStopWord(words);
    }

    public static List<String> readWordFromFile(InputStream is) {
        List<String> words;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            words = new ArrayList<String>(1200);
            for (String buf = ""; (buf = br.readLine()) != null; ) {
                if (buf == null || buf.trim().equals(""))
                    continue;
                words.add(buf);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
            }
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
        }
        return words;
    }

    /**
     * 增加停顿词
     *
     * @param words
     */
    public void addStopWord(final List<String> words) {
        if (!isEmpty(words)) {
            char[] chs;
            for (String curr : words) {
                chs = curr.toCharArray();
                for (char c : chs) {
                    sStopwdSet.add(charConvert(c));
                }
            }
        }
    }

    /**
     * 添加DFA节点
     *
     * @param words
     */
    public void addSensitiveWord(final List<String> words) {
        if (!isEmpty(words)) {
            char[] chs;
            int fchar;
            int lastIndex;
            WordNode fnode; // 首字母节点
            for (String curr : words) {
                chs = curr.toCharArray();
                fchar = charConvert(chs[0]);
                if (!sSet.contains(fchar)) {// 没有首字定义
                    sSet.add(fchar);// 首字标志位 可重复add,反正判断了，不重复了
                    fnode = new WordNode(fchar, chs.length == 1);
                    sNodes.put(fchar, fnode);
                } else {
                    fnode = sNodes.get(fchar);
                    if (!fnode.isLast() && chs.length == 1)
                        fnode.setLast(true);
                }
                lastIndex = chs.length - 1;
                for (int i = 1; i < chs.length; i++) {
                    fnode = fnode.addIfNoExist(charConvert(chs[i]), i == lastIndex);
                }
            }
        }
    }

    /**
     * 过滤判断 将敏感词转化为成屏蔽词
     *
     * @param src
     * @return
     */
    public final FilterContent doFilter(final String src) {
        FilterContent fc = new FilterContent();
        fc.content = src;
        if (sSet != null && sNodes != null && !TextUtils.isEmpty(src)) {
            char[] chs = src.toCharArray();
            int length = chs.length;
            int currc; // 当前检查的字符
            int cpcurrc; // 当前检查字符的备份
            int k;
            WordNode node;
            for (int i = 0; i < length; i++) {
                currc = charConvert(chs[i]);
                if (!sSet.contains(currc)) {
                    continue;
                }
                node = sNodes.get(currc);// 日 2
                if (node == null)// 其实不会发生，习惯性写上了
                    continue;
                boolean couldMark = false;
                int markNum = -1;
                if (node.isLast()) {// 单字匹配（日）
                    couldMark = true;
                    markNum = 0;
                }
                // 继续匹配（日你/日你妹），以长的优先
                // 你-3 妹-4 夫-5
                k = i;
                cpcurrc = currc; // 当前字符的拷贝
                for (; ++k < length; ) {
                    int temp = charConvert(chs[k]);
                    if (temp == cpcurrc)
                        continue;
                    if (sStopwdSet != null && sStopwdSet.contains(temp))
                        continue;
                    node = node.querySub(temp);
                    if (node == null)// 没有了
                        break;
                    if (node.isLast()) {
                        couldMark = true;
                        markNum = k - i;// 3-2
                    }
                    cpcurrc = temp;
                }
                if (couldMark) {
                    for (k = 0; k <= markNum; k++) {
                        chs[k + i] = MASKER;
                    }
                    String word = src.substring(i, i + markNum + 1);
                    if (!fc.words.contains(word))
                        fc.words.add(word);
                    i = i + markNum;
                }
            }
            fc.setMaskContent(new String(chs));
            return fc;
        }

        return fc;
    }

    /**
     * 大写转化为小写 全角转化为半角
     *
     * @param src
     * @return
     */
    public int charConvert(char src) {
        int r = BCConvert.qj2bj(src);
        return (r >= 'A' && r <= 'Z') ? r + 32 : r;
    }

    /**
     * 判断一个集合是否为空
     *
     * @param col
     * @return
     */
    public <T> boolean isEmpty(final Collection<T> col) {
        if (col == null || col.isEmpty()) {
            return true;
        }
        return false;
    }
}
