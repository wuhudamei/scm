package com.mdni.scm.service.prepareorder;

import com.mdni.scm.common.Constants;
import com.mdni.scm.common.dto.BootstrapPage;
import com.mdni.scm.common.dto.StatusDto;
import com.mdni.scm.common.service.CrudService;
import com.mdni.scm.common.utils.CodeGenerator;
import com.mdni.scm.common.utils.WebUtils;
import com.mdni.scm.entity.eum.*;
import com.mdni.scm.entity.order.IndentOrder;
import com.mdni.scm.entity.order.OrderItem;
import com.mdni.scm.entity.prepareorder.IndentPrepareOrder;
import com.mdni.scm.entity.prepareorder.IndentPrepareOrderItem;
import com.mdni.scm.entity.prod.Brand;
import com.mdni.scm.entity.prod.Product;
import com.mdni.scm.entity.prod.Sku;
import com.mdni.scm.repository.order.IndentOrderDao;
import com.mdni.scm.repository.order.OrderItemDao;
import com.mdni.scm.repository.prepareorder.IndentPrepareOrderDao;
import com.mdni.scm.repository.prepareorder.IndentPrepareOrderItemDao;
import com.mdni.scm.shiro.ShiroUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 订货预备单service
 * @Company: 美得你智装科技有限公司
 * @Author: Paul
 * @Date: 2017/12/19 19:00.
 */
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


    /**
     * 查询列表
     * @param params
     * @return
     */
    @Override
    public BootstrapPage<IndentPrepareOrder> searchScrollPage(Map<String, Object> params) {

        List<IndentPrepareOrder> pageData = Collections.emptyList();
        Long count = this.indentPrepareOrderDao.searchTotal(params);
        if (count > 0) {
            pageData = indentPrepareOrderDao.search(params);
            //遍历,给 installation_location 去重
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


    /**
     * 转订货单: 存储订货单及其对应的item
     *  一.需要根据每个预备单item的供应商Id拆分, 一个供应商Id对应一个备货单
     *  注意: 当前mybatis版本过低,批量插入不能返回主键,只能在循环中多次插入
     * @param id 预备单id
     * @author Paul
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Object turnToIndentOrder(Long id) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if(loggedUser == null){
            return StatusDto.buildFailureStatusDto("回话失效,请重新登录!");
        }

        //通过id查询预备单及对应的预备单item
        IndentPrepareOrder prepareOrder =  indentPrepareOrderDao.getWithItemById(id);
        if(prepareOrder != null) {
            Date now = new Date();
            //批次号
            String branchNo = "branchNo" + now;

            //待保存 订货单item集合
            List<OrderItem> toSaveOrderItemList = new ArrayList<OrderItem>();

            //1.获取预备单Item集合
            List<IndentPrepareOrderItem> pItemList = prepareOrder.getIndentPrepareOrderItemList();
            if (pItemList != null && pItemList.size() > 0) {
                //2.根据供应商id分组
                Map<Long, List<IndentPrepareOrderItem>> itemMapBySupplierId = pItemList.stream()
                        .filter(item -> item.getSupplierId() != null)
                        .collect(Collectors.groupingBy(item -> item.getSupplierId()));

                if (itemMapBySupplierId != null && itemMapBySupplierId.size() > 0) {
                    itemMapBySupplierId.forEach((supplierId, pItems) -> {

                        //二. 一个供应商Id 对应一个 订货单
                        IndentOrder indentOrder = new IndentOrder();
                        indentOrder.setCode(CodeGenerator.generateCode(Constants.ORDER_CODE));
                        indentOrder.setContractCode(prepareOrder.getContractCode());
                        indentOrder.setStatus(OrderStatusEnum.DRAFT);
                        //订货单说明
                        indentOrder.setNote("预备单转为订货单");
                        indentOrder.setCreator(loggedUser.valueOf());
                        indentOrder.setCreateTime(now);
                        //制单类型: 正常下单
                        indentOrder.setPlaceEnum(PlaceEnum.NORMAL);
                        //批次号
                        indentOrder.setBranchNo(branchNo);
                        //未接收
                        indentOrder.setAcceptStatus(OrderAcceptStatusEnum.NO);
                        //未下载
                        indentOrder.setDownloadNumber(0L);
                        //构建一个子类,并给供应商Id赋值,建立一个主表和字表的对应关系!
                        OrderItem itemTemp = new OrderItem();
                        itemTemp.setSupplierId(supplierId);
                        List<OrderItem> orderItemsTemp = new ArrayList<>();
                        orderItemsTemp.add(itemTemp);
                        indentOrder.setOrderItemList(orderItemsTemp);
                        //保存
                        indentOrderDao.insert(indentOrder);

                        if(pItems != null && pItems.size() > 0){
                            //获取该供应商对应的item集合,并遍历,pItems集合几个对象,就有几个OrderItem
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

                                //供货商Id
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
                                //支付状态--待结算
                                item.setPayStatus(PayStatusEnum.NOT_PAIED);
                                item.setStatus(SendStatusEnum.STAY_STORAGE);
                                //添加
                                toSaveOrderItemList.add(item);
                            }
                        }
                    });
                    if(toSaveOrderItemList.size() > 0){
                        //保存 item
                        orderItemDao.batchInsertList(toSaveOrderItemList);
                    }
                }
            }
            //修改预备单状态
            prepareOrder.setStatus(PrepareOrderEnum.ALREADY_TRANSFERRED.toString());
            prepareOrder.setSwitchTime(now);
            prepareOrder.setUpdateTime(now);
            prepareOrder.setUpdateAccount(loggedUser.getName() + "(" + loggedUser.getLoginName() + ")");
            indentPrepareOrderDao.update(prepareOrder);
            return StatusDto.buildSuccessStatusDto("转订货单成功!");
        }
        return StatusDto.buildFailureStatusDto("未查询到预备单!");
    }

    /**
     * 根据预备单id获取预备单项
     * @param prepareOrderId 预备单id
     * @return
     */
    public IndentPrepareOrder getByPrepareOrderId(Long prepareOrderId){
        IndentPrepareOrder indentPrepareOrder = this.entityDao.getById(prepareOrderId);
        //获取预备单项
        if(indentPrepareOrder != null){
            List<IndentPrepareOrderItem> prepareOrderItems = this.indentPrepareOrderItemDao.getByPrepareOrderId(indentPrepareOrder.getId());
            indentPrepareOrder.setIndentPrepareOrderItemList(prepareOrderItems);
        }
        return indentPrepareOrder;
    }

    /**
     * 从数据库中查询,并回填主键
     * @param orderList 需要填充主键的集合
     * @author Paul
     */
    private void dealPrimaryKeyWithList(List<IndentOrder> orderList) {
        Map<String, Object> parmas = new HashMap<String, Object>();
        //批次号
        parmas.put("branchNo", orderList.get(0).getBranchNo());
        parmas.put("contractCode", orderList.get(0).getContractCode());
        parmas.put("createTime", orderList.get(0).getCreateTime());
        List<IndentOrder> dbOrderList = indentOrderDao.findByQuery(parmas);

        if(dbOrderList != null && dbOrderList.size() > 0){
            //code为键
            Map<String, IndentOrder> dbOrderMap = dbOrderList.stream().filter(order -> StringUtils.isNotBlank(order.getCode()))
                    .collect(Collectors.toMap(k -> k.getCode(), v -> v));

            if(dbOrderMap != null && dbOrderMap.size() > 0){
                for(IndentOrder order : orderList){
                    //从dbOrderMap,通过code 取出 备货单,并设置id
                    IndentOrder orderTemp = dbOrderMap.get(order.getCode());
                    order.setId(orderTemp != null ? orderTemp.getId() : null);
                }
            }
        }

    }
}
