package cn.damei.scm.rest.prepareorder;

import cn.damei.scm.entity.prepareorder.IndentPrepareOrder;
import cn.damei.scm.common.BaseComController;
import cn.damei.scm.service.prepareorder.IndentPrepareOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material/indentprepareorderitem")
public class IndentPrepareOrderItemController extends BaseComController<IndentPrepareOrderService, IndentPrepareOrder> {
    

}
