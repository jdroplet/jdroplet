package jdroplet.freemarker.core.tmm;

import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import jdroplet.util.PinyinUtil;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ToPinyin implements TemplateMethodModel {

	@Override
	public Object exec(List args) throws TemplateModelException {
		if (args.size() != 1) {
			throw new TemplateModelException("Wrong arguments");
		}
		
		String v1 = String.valueOf(args.get(0));

		return PinyinUtil.toPinYin(v1);
	}

}
