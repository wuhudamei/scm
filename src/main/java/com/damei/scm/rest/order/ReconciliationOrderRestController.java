package com.damei.scm.rest.order;

import com.google.common.collect.Lists;
import com.damei.scm.common.BaseComController;
import com.damei.scm.common.dto.StatusDto;
import com.damei.scm.common.utils.ExcelUtil;
import com.damei.scm.common.utils.WebUtils;
import com.damei.scm.entity.order.ReconciliationOrderItem;
import com.damei.scm.service.order.ReconciliationOrderService;
import com.damei.scm.shiro.ShiroUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/reconciliation")
public class ReconciliationOrderRestController extends BaseComController<ReconciliationOrderService, ReconciliationOrderItem> {
    @Autowired
    private ReconciliationOrderService reconciliationOrderService;

    @RequestMapping("/findByReconciliation")
    public Object findByStatus(@RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endTime){
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ReconciliationOrderItem> list = this.reconciliationOrderService.findByReconciliation(managedSupplierIdList,startTime,endTime);
        return StatusDto.buildDataSuccessStatusDto(list);
    }
    @RequestMapping("/update")
    public Object update(HttpServletRequest request){
        if (WebUtils.getLoggedUser() == null) {
            return StatusDto.buildFailureStatusDto("未登录");
        }
        String[] ids = request.getParameterValues("ids[]");
        List<Long> idList = new ArrayList<>();
        for(int i = 0 ; i<ids.length;i++){
            idList.add(Long.parseLong(ids[i]));
        }
        Date payTime = new Date();
        ShiroUser shiroUser = WebUtils.getLoggedUser();
        Long operator = shiroUser.getId();
        this.reconciliationOrderService.updateBatch(idList,payTime,operator);
        return StatusDto.buildSuccessStatusDto("对账成功！");
    }

    @RequestMapping("/findByPayTime")
    public Object findByPayTime(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String startTime,
                               @RequestParam(required = false) String endTime){
        List<Long> managedSupplierIdList = WebUtils.getManagedSupplierIdsOfLoginUser();
        if (managedSupplierIdList != null && managedSupplierIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ReconciliationOrderItem> list = this.reconciliationOrderService.findByPayTime(managedSupplierIdList,keyword,startTime,endTime);
        return StatusDto.buildDataSuccessStatusDto(list);
    }

    @RequestMapping("/export")
    public void export(HttpServletResponse resp,
                       @RequestParam List<Long> ids) {
        ServletOutputStream out = null;
        Workbook workbook = null;
        List<ReconciliationOrderItem> list = this.reconciliationOrderService.findCheckOnWork(ids);
        try {
            resp.setContentType("application/x-msdownload");
            resp.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode("对账表.xlsx", "UTF-8") + "\"");
            out = resp.getOutputStream();
            if (CollectionUtils.isNotEmpty(list)) {
                workbook = ExcelUtil.getInstance().exportObj2ExcelWithTitleAndFields(list,ReconciliationOrderItem.class, true, titles, fields);
            }
            if (workbook != null) {
                workbook.write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
    private static List<String> titles = null;
    private static List<String> fields = null;

    static {
        titles = Lists.newArrayListWithExpectedSize(3);
        titles.add("合同编号");
        titles.add("订单编号");
        titles.add("商品名称");
        titles.add("商品型号");
        titles.add("商品规格");
        titles.add("商品属性1");
        titles.add("商品属性2");
        titles.add("商品属性3");
        titles.add("单价");
        titles.add("订货数量");
        titles.add("其他费用");
        titles.add("总金额");

        fields = Lists.newArrayListWithExpectedSize(3);
        fields.add("contractCode");
        fields.add("orderCode");
        fields.add("skuName");
        fields.add("model");
        fields.add("spec");
        fields.add("attribute1");
        fields.add("attribute2");
        fields.add("attribute3");
        fields.add("supplyPrice");
        fields.add("quantity");
        fields.add("otherFee");
        fields.add("totalMoney");
    }
}
