package com.damei.scm.service.dataArrangement;

import com.google.common.collect.Maps;
import com.damei.scm.common.service.CrudService;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.dataArrangement.MaterialCustomerContract;
import com.damei.scm.entity.dataArrangement.MaterialCustomerContractConfirm;
import com.damei.scm.entity.eum.dataArrangement.MaterialCustomerContractDataTypeEnum;
import com.damei.scm.entity.eum.dataArrangement.MetarialContractStatusEnum;
import com.damei.scm.repository.dataArrangement.MaterialCustomerContractConfirmDao;
import com.damei.scm.repository.dataArrangement.MaterialCustomerContractDao;
import com.damei.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

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
