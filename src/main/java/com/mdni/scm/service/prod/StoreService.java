package com.mdni.scm.service.prod;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.mdni.scm.common.PropertyHolder;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.HttpUtils;
import com.mdni.scm.common.utils.JsonDealUtils;
import com.mdni.scm.entity.prod.Store;
import com.mdni.scm.repository.prod.StoreDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StoreService extends CrudService<StoreDao, Store> {

    /**
     * 校验名称是否存在
     */
    public boolean checkNameExists(final Store store) {
        if (store == null || StringUtils.isBlank(store.getName())) {
            return false;
        }

        return this.entityDao.getByName(store.getName(), store.getId()) != null;
    }


    /**
     * 增加或者更新门店
     *
     * @param store
     */
    public void saveOrUpdate(final Store store) {
        if (store == null) {
            return;
        }

        if (store.getId() != null) {
            entityDao.update(store);
        } else {
            entityDao.insert(store);
        }
    }


    /**
     * 通过门店id列表 查询
     *
     * @param storeCodeList 门店id列表
     */
    public Map<Long, Store> findStoreIdKeyMap(List<Long> storeCodeList) {
        if (CollectionUtils.isEmpty(storeCodeList)) {
            return MapUtils.EMPTY_MAP;
        }

        List<Store> storeList = this.entityDao.findByIdIn(storeCodeList);
        if (CollectionUtils.isEmpty(storeList)) {
            return MapUtils.EMPTY_MAP;
        }

        return Maps.uniqueIndex(storeList, new Function<Store, Long>() {
            @Override
            public Long apply(Store input) {
                return input.getId();
            }
        });
    }

    /**
     * 通过门店编码 查询 门店信息
     *
     * @param code 门店编码
     * @return 门店信息
     */
    public Store getByCode(String code) {
        return this.entityDao.getByCode(code);
    }

    /**
     * 调用综管系统门店数据，更新本系统门店信息
     *  根据门店编码进行验重，对于存在的门店不处理，系统不存在的门店新增
     * @return boolean
     */
    public boolean getFromDateCenter() {
        boolean resultFlag = false;
        try {
            //请求用户账号接口
            String appUserRespResult = HttpUtils.get(PropertyHolder.getOaStoreCogradientUrl(), null);
            logger.info("调用综管系统获取门店接口，返回结果：" + appUserRespResult);
            Map<String, Object> resultMap = JsonDealUtils.fromJsonAsMap(appUserRespResult,String.class,Object.class);
            if( "1".equals( resultMap.get("code").toString() ) ){
                List<Map<String,Object>> storeData = (List<Map<String,Object>>)resultMap.get("data");
                //获取系统中现有的门店信息
                List<Store> allStoreList = this.entityDao.findAll();
                Map<String,Store> allStoreMap = Maps.newHashMap();
                for( Store store : allStoreList ){
                    allStoreMap.put(store.getCode(),store);
                }

                List<Store> insertStoreList = new ArrayList<>();
                if( storeData.size() > 0 ){
                    for( Map<String,Object> map : storeData ){
                        if( allStoreMap.get( map.get("orgCode") ) == null ){
                            Store store  = new Store();
                            store.setName( map.get("orgName").toString() );
                            store.setCode( map.get("orgCode").toString() );
                            insertStoreList.add( store );
                        }
                    }
                }
                //批量增加门店
                if( insertStoreList.size() > 0 ){
                    this.entityDao.batchInsert(insertStoreList);
                }

                resultFlag = true;
            }else{
                logger.error("调用综管系统获取门店接口失败" + resultMap.get("message"));
            }
        } catch (Exception e) {
            logger.error("调用综管系统获取门店接口出现异常，异常信息" + e.getMessage());
        }
        return resultFlag;
    }
}
