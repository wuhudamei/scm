package cn.damei.scm.service.prod;

import cn.damei.scm.common.PropertyHolder;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.common.utils.HttpUtils;
import cn.damei.scm.common.utils.JsonDealUtils;
import cn.damei.scm.entity.prod.Store;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import cn.damei.scm.repository.prod.StoreDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StoreService extends CrudService<StoreDao, Store> {

    public boolean checkNameExists(final Store store) {
        if (store == null || StringUtils.isBlank(store.getName())) {
            return false;
        }

        return this.entityDao.getByName(store.getName(), store.getId()) != null;
    }

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

    public Store getByCode(String code) {
        return this.entityDao.getByCode(code);
    }

    public boolean getFromDateCenter() {
        boolean resultFlag = false;
        try {
            String appUserRespResult = HttpUtils.get(PropertyHolder.getOaStoreCogradientUrl(), null);
            logger.info("调用综管系统获取门店接口，返回结果：" + appUserRespResult);
            Map<String, Object> resultMap = JsonDealUtils.fromJsonAsMap(appUserRespResult,String.class,Object.class);
            if( "1".equals( resultMap.get("code").toString() ) ){
                List<Map<String,Object>> storeData = (List<Map<String,Object>>)resultMap.get("data");
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
