package com.mdni.scm.service.dataArrangement;

import com.google.common.collect.Maps;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContract;
import com.mdni.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.mdni.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import com.mdni.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import com.mdni.scm.repository.dataArrangement.MaterialCustomerContractConfirmDao;
import com.mdni.scm.repository.dataArrangement.MaterialCustomerContractDao;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * Created by 巢帅 on 2017/8/4.
 */
@Service
public class MaterialCustomerContractService extends CrudService<MaterialCustomerContractDao,MaterialCustomerContract> {

    @Autowired
    MaterialCustomerContractConfirmDao  materialCustomerContractConfirmDao;

    public void updateStatus(Long id,MetarialContractStatusEnum contractStatus){
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String verifier = loggedUser.getName();
        Date reviewTime = new Date();
        MaterialCustomerContract customerContract = new MaterialCustomerContract();
        customerContract.setId(id);
        customerContract.setContractStatus(contractStatus);
        customerContract.setCreateTime(reviewTime);
        customerContract.setCreateAccount(WebUtils.getLoggedUser().getLoginName());
        customerContract.setKeyboarder(verifier);
        customerContract.setInputTime(reviewTime);
        this.entityDao.update(customerContract);
    }

    /**
     * 保存确认数据
     * @param materialCustomerContractConfirm
     */
    @Transactional
    public void saveConfirmData(MaterialCustomerContractConfirm materialCustomerContractConfirm){
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        String keyboarder = loggedUser.getName();
        Date inputTime = new Date();
        try{
            if( materialCustomerContractConfirm.getId() != null ){
                materialCustomerContractConfirmDao.update(materialCustomerContractConfirm);
            }else{
                if(isChange(materialCustomerContractConfirm)) {
                    materialCustomerContractConfirmDao.insert(materialCustomerContractConfirm);
                }
            }
            this.entityDao.updateStatus(materialCustomerContractConfirm.getOriginalId(),
                                        keyboarder,
                                        inputTime  ,
                                        MetarialContractStatusEnum.CHECKED);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据合同状态查询录入数据或者核查数据
     * @param id
     * @return
     */
    public Map<String,Object> getContractDetail(Long id){
        Map<String,Object> result = Maps.newHashMap();

        MaterialCustomerContract MaterialCustomerContract = this.entityDao.getById(id);

        result.put("contractBaseInfo",MaterialCustomerContract);
        result.put("contractStatus",MaterialCustomerContract.getContractStatus());
        result.put("contractInputInfo",this.materialCustomerContractConfirmDao.getByOriginalIdAndDataType(id, MaterialCustomerContractDataTypeEnum.INPUT));
        if( MaterialCustomerContract.getContractStatus() .equals(MetarialContractStatusEnum.COMPLETED) ){
            result.put("contractCheckInfo",this.materialCustomerContractConfirmDao.getByOriginalIdAndDataType(id, MaterialCustomerContractDataTypeEnum.CHECK));
        }
        return result;
    }

    /**
     * 插入校验
     * @param materialCustomerContractConfirm
     * @return
     */
    private Boolean isChange(MaterialCustomerContractConfirm materialCustomerContractConfirm){
        if( StringUtils.isNotBlank( materialCustomerContractConfirm.getBudgetNo() ) ){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getProjectCode() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getCustomerName() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getCustomerPhone() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getProjectAddress() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getMeal() )){
            return true;
        }
        if( materialCustomerContractConfirm.getBudgetArea() != null ){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getHouseLayout() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getDesignerName() )){
            return true;
        }
        if(StringUtils.isNotBlank( materialCustomerContractConfirm.getDesignerPhone() )){
            return true;
        }
        if( materialCustomerContractConfirm.getEngineeringCost() != null ){
            return true;
        }
        if( materialCustomerContractConfirm.getDismantleFee() != null ){
            return true;
        }
        if( materialCustomerContractConfirm.getHaveElevator() != null){
            return true;
        }
        if( materialCustomerContractConfirm.getContractSignDate() != null ){
            return true;
        }
        return false;
    }
}
