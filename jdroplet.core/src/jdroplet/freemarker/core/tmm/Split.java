package jdroplet.freemarker.core.tmm;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Created by kuibo on 2018/6/6.
 */
public class Split implements TemplateMethodModel {

    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 2) {
            throw new TemplateModelException("Wrong arguments");
        }

        String v1 = String.valueOf(args.get(0));
        String v2 = String.valueOf(args.get(1));

        return v1.split(v2);
    }

}
