package jdroplet.freemarker.core.tmm;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import jdroplet.plugin.PluginFactory;

import java.util.List;

/**
 * Created by kuibo on 2017/12/13.
 */
public class Render implements TemplateMethodModelEx {
    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() == 0) {
            throw new TemplateModelException("Wrong arguments");
        }
        String key = args.get(0).toString();
        Object[] new_args = new Object[args.size() - 1];
        for (int i = 0; i < new_args.length; i++) {
            Object arg = args.get(i + 1);

            if (arg instanceof SimpleNumber)
                new_args[i] = ((SimpleNumber) arg).getAsNumber().intValue();
            else
                new_args[i] = args.get(i + 1);
        }
        return PluginFactory.getInstance().applyFilters("Render" + key, "", new_args);
    }
}
