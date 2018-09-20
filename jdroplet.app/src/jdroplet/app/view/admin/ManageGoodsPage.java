package jdroplet.app.view.admin;

import jdroplet.bll.*;
import jdroplet.core.SiteUrls;
import jdroplet.core.SystemConfig;
import jdroplet.data.model.Goods;
import jdroplet.data.model.GoodsSpec;
import jdroplet.data.model.Section;
import jdroplet.data.model.Shop;
import jdroplet.enums.SortOrder;
import jdroplet.enums.SortSectionBy;
import jdroplet.util.DataSet;
import jdroplet.util.JSONUtil;
import jdroplet.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by kuibo on 2018/6/22.
 */
public class ManageGoodsPage extends ManageMasterPage {

    public void list() {
        Integer shopId = request.getIntParameter("shopId");
        Integer pageIndex = request.getIntParameter("pageIndex");
        Integer pageSize = request.getIntParameter("pageSize", 20);
        Integer sectionId1 = request.getIntParameter("secitonId1");
        Integer sectionId2 = request.getIntParameter("secitonId2");
        Integer sectionId3 = request.getIntParameter("secitonId3");
        Integer status = request.getIntParameter("status");
        String terms = request.getParameter("terms");
        DataSet<Goods> datas = null;
        Shop shop = null;

        if (shopId != null)
            shop = Shops.getShop(shopId);
        else
            shop = new Shop();

        addItem("shop", shop);

        datas = GoodsX.getGoods(shopId, sectionId1, sectionId2, sectionId3, terms, status, pageIndex, pageSize);
        addItem("datas", datas);
        addItem("shopId", shopId);
    }

    private Integer getPropertyId(Integer shopId, Integer parentId, String type, String name) {
        Integer propertyId = null;

        if (TextUtils.isEmpty(name))
            return null;

        propertyId = Sections.getSectionByName(type, name);
        if (propertyId == null) {
            Section section = new Section();

            section.setParentId(parentId);
            section.setName(name);
            section.setType(type);
            section.setShopId(shopId);
            propertyId = Sections.save(section);
        }

        return propertyId;
    }

    private Map getProperty(Integer shopId, Integer parentId, Integer goodsId, Integer propertyIndex) {
        Map map = new HashMap<>();
        Section sec = null;
        DataSet<Section> datas = null;

        sec = Sections.getSection(parentId);
        map.put("type", sec.getName());
        map.put("children", new ArrayList());

        datas = Sections.getSections(shopId, parentId, "goods_property_value", SortSectionBy.ID, SortOrder.ASC, 1, Integer.MAX_VALUE);
        for(Section s:datas.getObjects()) {
            GoodsSpec spec = null;

            if (propertyIndex == 1)
                spec = GoodsSpecs.getGoodsSpec(goodsId, 1, s.getId(), null, null);
            else if (propertyIndex == 2)
                spec = GoodsSpecs.getGoodsSpec(goodsId, 1, null, s.getId(), null);
            else if (propertyIndex == 3)
                spec = GoodsSpecs.getGoodsSpec(goodsId, 1, null, null, s.getId());

            if (spec != null)
                ((List)map.get("children")).add(s.getName());
        }
        return map;
    }

    public void edit() {
        Integer id = request.getIntParameter("id", 0);
        Goods goods = null;

        if (id == null || id == 0) {
            Integer shopId = request.getIntParameter("shopId");
            goods = new Goods();
            goods.setId(0);
        } else {
            goods = GoodsX.getGoods(id);
            shopId = goods.getShopId();
        }

        if (!request.isPostBack()) {
            addItem("id", id);
            addItem("shopId", shopId);
            addItem("goods", goods);

            List<Map<String, Object>> list = new ArrayList();
            Map<String, Object> map = null;
            Map<String, Object> item = null;
            List<GoodsSpec> goodsSpecs = null;
            Section sec = null;

            goodsSpecs = GoodsSpecs.getGoodsSpecs(shopId, id, null, GoodsSpec.NORMAL);
            for(GoodsSpec spec:goodsSpecs) {
                item = JSONUtil.toMap(spec);

                map = new HashMap<>();
                map.put("goodsSpec", item);

                sec = Sections.getSection(spec.getProperty1());
                if (sec == null)
                    map.put("property0", "");
                else
                    map.put("property0", sec.getName());

                sec = Sections.getSection(spec.getProperty2());
                if (sec == null)
                    map.put("property1", "");
                else
                    map.put("property1", sec.getName());


                sec = Sections.getSection(spec.getProperty3());
                if (sec == null)
                    map.put("property2", "");
                else
                    map.put("property2", sec.getName());

                item.put("properties", new ArrayList<>());
                ((List)item.get("properties")).add(map.get("property0"));
                ((List)item.get("properties")).add(map.get("property1"));
                ((List)item.get("properties")).add(map.get("property2"));
                list.add(map);
            }
            addItem("goodsSpecs", JSONUtil.toJSONString(list));

            Integer property1 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName1());
            Integer property2 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName2());
            Integer property3 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName3());
            List<Map<String, Object>> pps = new ArrayList();
            Map<String, Object> ppm = null;

            if (property1 != null) {
                pps.add(getProperty(shopId, property1, id, 1));
            }

            if (property2 != null) {
                pps.add(getProperty(shopId, property2, id, 2));
            }

            if (property3 != null) {
                pps.add(getProperty(shopId, property3, id, 3));
            }
            addItem("properties", JSONUtil.toJSONString(pps));
            if(goods.getPics() == null)
                addItem("pics", "[]");
            else
                addItem("pics", JSONUtil.toJSONString(goods.getPics()));

            Integer[] sections = new Integer[]{goods.getSectionId1(), goods.getSectionId2(), goods.getSectionId3()};
            addItem("sections", JSONUtil.toJSONString(sections));
            addItem("atta_path", SystemConfig.getAttachmentsStorePath());
        } else {
            Integer shopId = request.getIntParameter("shop_id");
            Goods.DataColumns.fill(request, goods);

            Integer property1 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName1());
            Integer property2 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName2());
            Integer property3 = getPropertyId(shopId, 0, "goods_property_name", goods.getPropertyName3());
            Integer specProperty1 = null;
            Integer specProperty2 = null;
            Integer specProperty3 = null;

            if (id == 0) {
                goods.setCreateTime(new Date());
            }
            id = GoodsX.save(goods);

            JSONArray jarr = request.getJSONArrayParameter("goodsSpecs");
            JSONArray jarr2 = null;
            JSONObject jobj = null;

            GoodsSpec spec = null;

            GoodsSpecs.updateStatus(id, GoodsSpec.OFFLINE);
            for (int i = 0; i < jarr.length(); i++) {
                jobj = jarr.optJSONObject(i);
                jobj = jobj.optJSONObject("goodsSpec");

                jarr2 = jobj.optJSONArray("properties");
                for (int j = 0; j < jarr2.length(); j++) {
                    String name = jarr2.optString(j);

                    if (j == 0) {
                        specProperty1 = getPropertyId(shopId, property1, "goods_property_value", name);
                    } else if (j == 1) {
                        specProperty2 = getPropertyId(shopId, property2, "goods_property_value", name);
                    } else if (j == 2) {
                        specProperty3 = getPropertyId(shopId, property3, "goods_property_value", name);
                    }
                }

                spec = new GoodsSpec();
                spec.setId(jobj.optInt("id"));
                spec.setShopId(shopId);
                spec.setGoodsId(id);
                spec.setCost((int) (jobj.optDouble("cost") * 100));
                spec.setPrice((int) (jobj.optDouble("price") * 100));
                spec.setSku(jobj.optString("sku"));
                spec.setStocks(jobj.optInt("stocks"));
                spec.setStatus(1);
                spec.setProperty1(specProperty1);
                spec.setProperty2(specProperty2);
                spec.setProperty3(specProperty3);

                if (spec.getId() == null || spec.getId() == 0) {
                    spec.setCreateTime(new Date());
                }

                GoodsSpecs.save(spec);
            }

            super.addNotices("更新成功");
            SiteUrls urls = null;
            urls = SystemConfig.getSiteUrls();
            response.setRedirect(urls.getUrl("goods.edit", id));
        }
    }
}
