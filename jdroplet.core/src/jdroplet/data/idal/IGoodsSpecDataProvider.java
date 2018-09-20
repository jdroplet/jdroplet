package jdroplet.data.idal;

import jdroplet.data.model.GoodsSpec;

/**
 * Created by kuibo on 2018/6/28.
 */
public interface IGoodsSpecDataProvider extends IDataProvider {

    GoodsSpec getGoodsSpec(Integer goodsId, Integer status, Integer property1, Integer property2, Integer property3);
}
