+(function() {
  var vueIndex = new Vue({
    el: '#container',
    mixins: [DaMeiVueMixins.DataTableMixin],
    data: {
      // 面包屑
      breadcrumbs: [{
        path: '/',
        name: '主页'
      }, {
        path: '/',
        name: 'xx 管理',
        active: true //激活面包屑的
      }],
      // 查询表单
      form: {
        keyword: '',
        status: '',
        company: ''
      },
      selectedRows: {}, //选中列
      modalModel: null, //模式窗体模型
      _$el: null, //自己的 el $对象
      _$dataTable: null //datatable $对象
    },
    created: function() {

    },
    ready: function() {
      this.drawTable();
    },
    methods: {
      query: function() {
        this.$dataTable.bootstrapTable('selectPage', 1);
      },
      drawTable: function() {
        var self = this;
        self.$dataTable = $('#dataTable', self._$el).bootstrapTable({
          url: '/js/order.json',
          method: 'get',
          dataType: 'json',
          cache: false, //去缓存
          pagination: true, //是否分页
          sidePagination: 'server', //服务端分页
          queryParams: function(params) {
            // 将table 参数与搜索表单参数合并
            return _.extend({}, params, self.form);
          },
          mobileResponsive:true,
          undefinedText: '-', //空数据的默认显示字符
          striped: true, //斑马线
          maintainSelected: true, //维护checkbox选项
          sortName: 'ordNo', //默认排序列名
          sortOrder: 'desc', //默认排序方式
          columns: [{
            checkbox: true,
            align: 'center'
          }, {
            field: 'ordNo',
            title: '订单号',
            align: 'center',
            sortable: true,
            order: 'desc'
          }, {
            field: 'receiverName',
            title: "会员名称",
            align: 'center',
            sortable: true
          }, {
            field: 'orderTotalPrice',
            title: "支付价格",
            align: 'center',
            sortable: true,
            formatter: function(value, row, index) {
              return '￥' + value;
            }
          }, {
            field: 'orderPayType',
            title: "支付方式",
            align: 'center',
            sortable: true,
            formatter: function(value, row, index) {
              // return orderPayFilter(value);
            }
          }, {
            field: 'orderStatus',
            title: "订单状态",
            align: 'center',
            sortable: true,
            formatter: function(value, row, index) {
              if (value == 0) {
                return '<button data-handle="operate-pay" data-id="' + row.ordNo + '" class="btn btn-xs btn-danger">确认支付</button>';
              } else {}
            }
          }, {
            field: 'created',
            title: "点单时间",
            align: 'center',
            sortable: true
          }, {
            field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
            title: "操作",
            align: 'center',
            formatter: function(value, row, index) {
              return '<button data-handle="operate-click" data-index="' + index + '" data-id="' + value + '" type="button" class="btn btn-xs btn-danger">查看</button>';
            }
          }]
        });

        self.checkEventHandle('ordNo');
      },
      createEmptyModel: function() {
        this.modalModel = {

        };
      },
      createBtnClickHandler: function(e) {

        var $modal = $('#modal').modal({
          height: 450,
          maxHeight: 450
        });
        modal($modal, {
          loginName: 'dewei',
          name: '德巍',
          id: 1000011,
          position: '码农',
          department: '技术部',

        }, false);
      }
    }
  });


  // 实现弹窗方法
  function modal($el, model, isEdit) {
    // 获取 node
    var el = $el.get(0);
    // 是否修改 boolean 化
    isEdit = !!isEdit;
    // 创建 Vue 对象编译节点
    var vueModal = new Vue({
      el: el,
      // 模式窗体必须引用 ModalMixin
      mixins: [DaMeiVueMixins.ModalMixin],
      $modal: $el, //模式窗体 jQuery 对象
      data: {
        //控制 按钮是否可点击
        disabled: false,
        //模型复制给对应的key
        user: model,
      },
      created: function() {

      },
      ready: function() {
        this.activeDatepicker();
      },
      methods: {
        activeDatepicker:function(){
          var self = this;
          $('#userBirthday',self._$el).datetimepicker({
          });
        }
      },
      beforeDestroy: function() {
        /*
         * 使用了 ModalMixin 会在 dom 删除前触发 Vue beforeDestroy 事件
         * 将绑定在表单和 window的事件处理全部解除
         *
         **/
         $('#userBirthday',self._$el).datetimepicker('remove');
        console.log('beforedestroy');
      }
    });



    // 创建的Vue对象应该被返回
    return vueModal;
  }
})();

