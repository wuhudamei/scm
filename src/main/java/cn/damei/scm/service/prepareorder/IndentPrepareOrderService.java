package cn.damei.scm.service.prepareorder;

import cn.damei.scm.common.Constants;
import cn.damei.scm.entity.eum.*;
import cn.damei.scm.entity.order.IndentOrder;
import cn.damei.scm.entity.prepareorder.IndentPrepareOrder;
import cn.damei.scm.entity.prod.Product;
import cn.damei.scm.repository.order.OrderItemDao;
import cn.damei.scm.repository.prepareorder.IndentPrepareOrderItemDao;
import cn.damei.scm.common.dto.BootstrapPage;
import cn.damei.scm.common.dto.StatusDto;
import cn.damei.scm.common.service.CrudService;
import cn.damei.scm.common.utils.CodeGenerator;
import cn.damei.scm.common.utils.WebUtils;
import cn.damei.scm.entity.order.OrderItem;
import cn.damei.scm.entity.prepareorder.IndentPrepareOrderItem;
import cn.damei.scm.entity.prod.Brand;
import cn.damei.scm.entity.prod.Sku;
import cn.damei.scm.repository.order.IndentOrderDao;
import cn.damei.scm.repository.prepareorder.IndentPrepareOrderDao;
import cn.damei.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndentPrepareOrderService extends CrudService<IndentPrepareOrderDao, IndentPrepareOrder> {

    @Autowired
    private IndentPrepareOrderDao indentPrepareOrderDao;
    @Autowired
    private IndentOrderDao indentOrderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private IndentPrepareOrderItemDao indentPrepareOrderItemDao;

    private Logger logger = LoggerFactory.getLogger(IndentPrepareOrderService.class);


    @Override
    public BootstrapPage<IndentPrepareOrder> searchScrollPage(Map<String, Object> params) {

        List<IndentPrepareOrder> pageData = Collections.emptyList();
        Long count = this.indentPrepareOrderDao.searchTotal(params);
        if (count > 0) {
            pageData = indentPrepareOrderDao.search(params);
            if(pageData.size() > 0){
                for(IndentPrepareOrder pOrder : pageData){
                    String installationLocation = pOrder.getInstallationLocation();
                    if(StringUtils.isNotBlank(installationLocation)){
                        StringBuilder instaBuilder = new StringBuilder();
                        Set<String> instSet = new HashSet<String>();
                        String[] instArr = installationLocation.split(",");
                        for(String str : instArr){
                            if(StringUtils.isNotBlank(str)){
                                instSet.add(str);
                            }
                        }
                        if(instSet.size() > 0){
                            instSet.forEach(str -> {
                                instaBuilder.append(str + ",");
                            });
                        }
                        if(instaBuilder.lastIndexOf(",") == instaBuilder.length() - 1){
                            instaBuilder.deleteCharAt(instaBuilder.length() - 1);
                        }
                        pOrder.setInstallationLocation(instaBuilder.toString());
                    }
                }
            }
        }
        return new BootstrapPage(count, pageData);
    }


    @Transactional(rollbackFor = Exception.class)
    public Object turnToIndentOrder(Long id) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if(loggedUser == null){
            return StatusDto.buildFailureStatusDto("回话失效,请重新登录!");
        }

        IndentPrepareOrder prepareOrder =  indentPrepareOrderDao.getWithItemById(id);
        if(prepareOrder != null) {
            Date now = new Date();
            String branchNo = "branchNo" + now;

            List<OrderItem> toSaveOrderItemList = new ArrayList<OrderItem>();

            List<IndentPrepareOrderItem> pItemList = prepareOrder.getIndentPrepareOrderItemList();
            if (pItemList != null && pItemList.size() > 0) {
                Map<Long, List<IndentPrepareOrderItem>> itemMapBySupplierId = pItemList.stream()
                        .filter(item -> item.getSupplierId() != null)
                        .collect(Collectors.groupingBy(item -> item.getSupplierId()));

                if (itemMapBySupplierId != null && itemMapBySupplierId.size() > 0) {
                    itemMapBySupplierId.forEach((supplierId, pItems) -> {

                        IndentOrder indentOrder = new IndentOrder();
                        indentOrder.setCode(CodeGenerator.generateCode(Constants.ORDER_CODE));
                        indentOrder.setContractCode(prepareOrder.getContractCode());
                        indentOrder.setStatus(OrderStatusEnum.DRAFT);
                        indentOrder.setNote("预备单转为订货单");
                        indentOrder.setCreator(loggedUser.valueOf());
                        indentOrder.setCreateTime(now);
                        indentOrder.setPlaceEnum(PlaceEnum.NORMAL);
                        indentOrder.setBranchNo(branchNo);
                        indentOrder.setAcceptStatus(OrderAcceptStatusEnum.NO);
                        indentOrder.setDownloadNumber(0L);
                        OrderItem itemTemp = new OrderItem();
                        itemTemp.setSupplierId(supplierId);
                        List<OrderItem> orderItemsTemp = new ArrayList<>();
                        orderItemsTemp.add(itemTemp);
                        indentOrder.setOrderItemList(orderItemsTemp);
                        indentOrderDao.insert(indentOrder);

                        if(pItems != null && pItems.size() > 0){
                            for(IndentPrepareOrderItem pIt : pItems){
                                OrderItem item = new OrderItem();

                                Product product = new Product();
                                product.setModel(pIt.getModel());
                                product.setSpec(pIt.getSpec());

                                Sku sku = new Sku();
                                sku.setId(pIt.getSkuId());
                                sku.setName(pIt.getSkuName());
                                sku.setProduct(product);
                                sku.setAttribute1(pIt.getAttribute1());
                                sku.setAttribute2(pIt.getAttribute2());
                                sku.setAttribute3(pIt.getAttribute3());

                                Brand brand = new Brand();
                                brand.setId(prepareOrder.getBrandId());
                                brand.setBrandName(prepareOrder.getBrandName());

                                item.setSupplierId(supplierId);
                                item.setOrderId(indentOrder.getId());
                                item.setSku(sku);
                                item.setSupplyPrice(pIt.getSupplyPrice());
                                item.setQuantity(pIt.getQuantity());
                                item.setBrand(brand);
                                item.setBrandId(prepareOrder.getBrandId());
                                item.setInstallationLocation(pIt.getInstallationLocation());
                                item.setSpecUnit(pIt.getSpecUnit());
                                item.setTabletNum(pIt.getTabletNum() != null ? pIt.getTabletNum().intValue() : 0);
                                item.setHasOtherFee(false);
                                item.setPayStatus(PayStatusEnum.NOT_PAIED);
                                item.setStatus(SendStatusEnum.STAY_STORAGE);
                                toSaveOrderItemList.add(item);
                            }
                        }
                    });
                    if(toSaveOrderItemList.size() > 0){
                        orderItemDao.batchInsertList(toSaveOrderItemList);
                    }
                }
            }
            prepareOrder.setStatus(PrepareOrderEnum.ALREADY_TRANSFERRED.toString());
            prepareOrder.setSwitchTime(now);
            prepareOrder.setUpdateTime(now);
            prepareOrder.setUpdateAccount(loggedUser.getName() + "(" + loggedUser.getLoginName() + ")");
            indentPrepareOrderDao.update(prepareOrder);
            return StatusDto.buildSuccessStatusDto("转订货单成功!");
        }
        return StatusDto.buildFailureStatusDto("未查询到预备单!");
    }

    public IndentPrepareOrder getByPrepareOrderId(Long prepareOrderId){
        IndentPrepareOrder indentPrepareOrder = this.entityDao.getById(prepareOrderId);
        if(indentPrepareOrder != null){
            List<IndentPrepareOrderItem> prepareOrderItems = this.indentPrepareOrderItemDao.getByPrepareOrderId(indentPrepareOrder.getId());
            indentPrepareOrder.setIndentPrepareOrderItemList(prepareOrderItems);
        }
        return indentPrepareOrder;
    }

    private void dealPrimaryKeyWithList(List<IndentOrder> orderList) {
        Map<String, Object> parmas = new HashMap<String, Object>();
        parmas.put("branchNo", orderList.get(0).getBranchNo());
        parmas.put("contractCode", orderList.get(0).getContractCode());
        parmas.put("createTime", orderList.get(0).getCreateTime());
        List<IndentOrder> dbOrderList = indentOrderDao.findByQuery(parmas);

        if(dbOrderList != null && dbOrderList.size() > 0){
            Map<String, IndentOrder> dbOrderMap = dbOrderList.stream().filter(order -> StringUtils.isNotBlank(order.getCode()))
                    .collect(Collectors.toMap(k -> k.getCode(), v -> v));

            if(dbOrderMap != null && dbOrderMap.size() > 0){
                for(IndentOrder order : orderList){
                    IndentOrder orderTemp = dbOrderMap.get(order.getCode());
                    order.setId(orderTemp != null ? orderTemp.getId() : null);
                }
            }
        }

    }
}
