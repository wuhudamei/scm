package com.damei.scm.rest.prepareorder;

import com.damei.scm.common.BaseComController;
import com.damei.scm.entity.prepareorder.IndentPrepareOrder;
import com.damei.scm.service.prepareorder.IndentPrepareOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/indentprepareorderitem")
public class IndentPrepareOrderItemController extends BaseComController<IndentPrepareOrderService, IndentPrepareOrder> {
    

}
