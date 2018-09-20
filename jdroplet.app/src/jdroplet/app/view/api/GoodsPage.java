package jdroplet.app.view.api;

import jdroplet.bll.GoodsSpecs;
import jdroplet.bll.GoodsX;
import jdroplet.bll.Sections;
import jdroplet.data.model.Goods;
import jdroplet.data.model.GoodsSpec;
import jdroplet.data.model.Section;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.util.DataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kuibo on 2018/7/23.
 */
public class GoodsPage extends APIPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize", 20);
        Integer sectionId1 = request.getIntParameter("sectionId1");
        Integer sectionId2 = request.getIntParameter("sectionId2");
        Integer sectionId3 = request.getIntParameter("sectionId3");
        Integer status = request.getIntParameter("status");
        String terms = request.getParameter("terms");
        DataSet<Goods> datas = null;

        datas = GoodsX.getGoods(shopId, sectionId1, sectionId2, sectionId3, terms, status, pageIndex, pageSize);
        renderJSON(0, null, datas);
    }


    public void get() {
        Integer goodsId = request.getIntParameter("id");
        Goods goods = GoodsX.getGoods(goodsId);
        List<GoodsSpec> specs = null;

        specs = GoodsSpecs.getGoodsSpecs(goods.getShopId(), goodsId, null, GoodsSpec.NORMAL);
        goods.setSpecs(specs);

        Integer property1 = Sections.getSectionByName("goods_property_name", goods.getPropertyName1());
        Integer property2 = Sections.getSectionByName("goods_property_name", goods.getPropertyName2());
        Integer property3 = Sections.getSectionByName("goods_property_name", goods.getPropertyName3());
        DataSet<Section> properties1 = Sections.getSections(null, property1, "", SortSectionBy.ID, SortOrder.ASC, 1, 10);
        DataSet<Section> properties2 = Sections.getSections(null, property2, "", SortSectionBy.ID, SortOrder.ASC, 1, 10);
        DataSet<Section> properties3 = Sections.getSections(null, property3, "", SortSectionBy.ID, SortOrder.ASC, 1, 10);
        Iterator<Section> it = null;
        List<Section> newSections = null;

        it = properties1.getObjects().iterator();
        newSections = new ArrayList<>();
        while (it.hasNext()) {
            Section s = it.next();
            GoodsSpec spec = GoodsSpecs.getGoodsSpec(goodsId, 1, s.getId(), null, null);
            if (spec != null) {
                newSections.add(s);
            }
        }
        properties1.setObjects(newSections);
        goods.setValue("properties1", newSections);

        //======
        it = properties2.getObjects().iterator();
        newSections = new ArrayList<>();
        while (it.hasNext()) {
            Section s = it.next();
            GoodsSpec spec = GoodsSpecs.getGoodsSpec(goodsId, 1, null, s.getId(), null);
            if (spec != null) {
                newSections.add(s);
            }
        }
        properties2.setObjects(newSections);
        goods.setValue("properties2", newSections);

        //======
        it = properties3.getObjects().iterator();
        newSections = new ArrayList<>();
        while (it.hasNext()) {
            Section s = it.next();
            GoodsSpec spec = GoodsSpecs.getGoodsSpec(goodsId, 1, null, null, s.getId());
            if (spec != null) {
                newSections.add(s);
            }
        }
        properties3.setObjects(newSections);
        goods.setValue("properties3", newSections);

        renderJSON(0, null, goods);
    }
}
