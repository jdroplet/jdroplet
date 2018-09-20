package jdroplet.freemarker.core.tmm;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

public class SingleLine implements TemplateMethodModel {

    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() > 2) {
            throw new TemplateModelException("Wrong arguments");
        }

        String v1 = String.valueOf(args.get(0));
        v1 = v1.replace("\n", "");

        if (args.size() == 2) {
            Integer len = Integer.valueOf((String) args.get(1));
            if (len > v1.length())
                len = v1.length();
            v1 = v1.substring(0, len);
        }
        return v1;
    }

}
