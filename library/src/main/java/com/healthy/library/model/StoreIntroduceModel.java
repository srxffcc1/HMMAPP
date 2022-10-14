package com.healthy.library.model;

import java.util.List;

/**
 * @author Li
 * @date 2019/03/04 17:26
 * @des 门店model
 */

public class StoreIntroduceModel {


        public int shopId;
        public String brandIntroduce;
        public String shopIntroduce;
        public String businessTime;
        public String shopName;
        public String headUrl;
        public String envUrl;
        public double commentMarkAvg;
        public List<CategoryNameServiceDTO> allCategoryNameServiceDTO;


    public class CategoryNameServiceDTO {

        public String categoryName;
    }
}
