package jdroplet.data.idal;

import jdroplet.data.model.ProductAtt;


import java.util.List;

/**
 * Created by kuibo on 2018/1/20.
 */
public interface ISkuDataProvider extends IDataProvider {

    List<ProductAtt> getAtts(Integer productId, Integer skuId);
}
