/**
 * Created by weiys on 16/4/21.
 * 依赖jquery  hogan jquery.form.min.js
 * 没有依赖对话 会报错
 *
 *使用例子:
 *var arry =[{title:'股票导入',addurl:ctx + '/api/dict/stock/import',downurl:ctx + "/static/excel/stock.xls"}]; 
 * ImportExcelUtil.init(arry,$('#importExcelDiv'));
 */
var ImportExcelUtil = {

    //上传excel表单
    importExcelForm :'<form id="importExcelForm" enctype="multipart/form-data">'
    +'<div class="modal-content animated bounceInRight">'
    +' <div class="modal-header">'
    +' <button type="button" class="close" data-dismiss="modal">'
    +' <span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>'
    +'  </button>'
    +' <h6 class="modal-title">{{title}}</h6>'
    +'</div>'
    +' <div class="modal-body">'
    +'  <div class="form-group">'
    +'<label>Excel数据：</label>'
    +' <input id="fileExcel" name="file" type="file" value=""/>'
    +' </div>'

    +' </div>'
    +' <div class="modal-footer">'
    +' <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>'
    +' <button data-handle="downLoadModal" type="button" style="width:100px;"  class="btn btn-outline btn-primary">下载模板</button>'
    +' <button data-handle="importBtn" type="button"  style="width:100px;" class="btn btn-outline btn-primary">导入</button>'
    +'</div>'
    +' </form>',

    //body元素jquery
    $winBody : $('body'),

    //第一次点击导入按钮父元素
    $btnPostion : {},

    //编译后对模板
    template : {},

    // 模式窗体
    modalExcelWin : {},

    //importBtnArray 数组,里边时对象 title addurl downurl ,$btnPostion导入按钮容器(布局用)
    init:function (importBtnArray,$btnPostion){
        this.$winBody.find('#modalExcelWin').remove();//先移除  再绑定
        this.$winBody.find('#downloadFrame').remove();//先移除  再绑定
        this.$winBody.prepend('<div id="modalExcelWin" class="modal fade" data-width="500"  tabindex="-1" role="dialog" aria-hidden="true"> </div>');
        this.$winBody.append('<iframe style="display: none; width: 1px; height: 1px;" id="downloadFrame"></iframe>');

        this.modalExcelWin=$('#modalExcelWin');
        this.$btnPostion = $btnPostion;
        //编译模版
        this.template = Hogan.compile(this.importExcelForm);
        //创建按钮对象
        var self = this;
        $(importBtnArray).each(function (index, ele) {
            self.$btnPostion.prepend('<button data-name="importBtn"  data-addurl="'+ele.addurl+'"  data-downurl="'+ele.downurl+'" data-title="'+ele.title+'" type="button"  class="btn btn-block btn-outline btn-primary">'+ele.title+'</button>');
        });

        this.bindEvent();
    },

    //并定点击事件
    bindEvent:function(){
        var self = this;
        this.$btnPostion.on('click','[data-name="importBtn"]', function(event) {
            self.importModalEvent($(this));
            event.stopPropagation();
            return false;
        });
    },

    //模式窗体事件绑定
    importModalEvent:function($thisBtn) {
        // 重置模式窗体内html
        this.modalExcelWin.html(this.template.render({
            title:$thisBtn.data('title')
        }));
        // 展示模式窗体
        this.modalExcelWin.modal({
            keyboard : true
        });


        // 导入
        this.modalExcelWin.off('click', '[data-handle="importBtn"]').on('click','[data-handle="importBtn"]', function(event) {
            var fileExcel = $("#fileExcel").val();
            if (fileExcel == "") {
                Vue.toastr.error("请选择上传文件");
                return;
            }

            $("#importExcelForm").ajaxSubmit({
                type : 'post', // 提交方式 get/post
                url : $thisBtn.data('addurl'), // 需要提交的
                // url
                success : function(data) { // data 保存提交后返回的数据，一般为
                    // json 数据
                    if (data.code == 1) {
                        Vue.toastr.success(data.message);
                    } else {
                        Vue.toastr.error(data.message);
                    }
                    $(this).resetForm(); // 提交后重置表单
                }
            });
           // event.stopPropagation();
        });
        this.modalExcelWin.off('click', '[data-handle="downLoadModal"]').on('click','[data-handle="downLoadModal"]', function(event) {
            $("#downloadFrame").attr("src", $thisBtn.data('downurl'));
            event.stopPropagation();
        });
    }

};