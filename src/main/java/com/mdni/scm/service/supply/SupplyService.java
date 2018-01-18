package com.mdni.scm.service.supply;


import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.entity.eum.PlaceEnum;
import com.mdni.scm.entity.supply.Supply;
import com.mdni.scm.repository.supply.SupplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13 0013.
 */
@Service
public class SupplyService extends CrudService<SupplyDao, Supply> {

    @Autowired
    private SupplyDao supplyDao;

    public Object findSupplyInfoByContractNo(String contractNo) {

        String branchNo = null;
        String createUser = "";
        Date creatTime = null;
        PlaceEnum placeStatus = null;
        List<Supply> tempList = new ArrayList<>();
        Map<String, Object> temp = Maps.newHashMap();
        List<Map<String, Object>> scmList = new ArrayList<>();
        List<Supply> list = this.supplyDao.findSupplyInfoByContractNo(contractNo);
        if (list != null && list.size() > 0) {
            for (Supply supply : list) {
                if (branchNo == null) {
                    tempList.add(supply);
                    branchNo = supply.getBranchNo();
                    createUser = supply.getCreator().getName();
                    creatTime = supply.getCreateTime();
                    placeStatus = supply.getPlaceStatus();
                } else {
                    if (branchNo.equals(supply.getBranchNo())) {//当变更号一致
                        tempList.add(supply);
                    } else {

                        temp.put("branchNo",branchNo);
                        temp.put("createUser", createUser);
                        temp.put("creatTime", creatTime);
                        temp.put("placeStatus", placeStatus);
                        temp.put("supplyList", tempList);
                        scmList.add(temp);

                        temp = null;
                        temp = Maps.newHashMap();
                        tempList = null;
                        tempList = new ArrayList<>();
                        tempList.add(supply);
                        branchNo = supply.getBranchNo();
                        createUser = supply.getCreator().getName();
                        creatTime = supply.getCreateTime();
                        placeStatus = supply.getPlaceStatus();
                    }
                }
            }
            temp.put("branchNo",branchNo);
            temp.put("createUser", createUser);
            temp.put("creatTime", creatTime);
            temp.put("placeStatus", placeStatus);
            temp.put("supplyList", tempList);
            scmList.add(temp);
        }

        return scmList;
    }
}
