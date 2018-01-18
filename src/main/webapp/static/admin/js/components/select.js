;

(function() {
  function EventProxy() {
    var closureObj = {};
    this.add = function(target, event, func, vm) {
      var ticket = Date.now();
      closureObj[ticket] = function() {
        func.call(vm);
      }
      target.addEventListener(event, closureObj[ticket], false);
      return ticket;
    }
    this.remove = function(target, event, ticket) {
      target.removeEventListener(event, closureObj[ticket], false);
      delete closureObj[ticket];
    }
  }
  var eventProxy = new EventProxy();


  var VueSelect2 = Vue.extend({
    name: 'vue-select2',
    props: {
      url: {
        type: String,
        default: '/api/reviewSize/supplierList'
      },
      classObj: {
        type: Object
      },
      selectedList: {
        type: Array,
        required: true
      },
      // placeholder
      placeholder: {
        type: String,
        default: '请选择'
      },
      // 可多选
      multiple: {
        type: Boolean,
        default: false
      },
      // 是否可直接按回车输入
      fakeAble: {
        type: Boolean,
        default: false
      },
      // 语言设置
      language: {
        type: String,
        default: 'zh-CN'
      },
      //用户可清除选项
      allowClear: {
        type: Boolean,
        default: false
      },
      // 是否在关闭 select 时选中高亮的
      selectOnClose: {
        type: Boolean,
        default: false
      },
      disabled: {
        type: Boolean,
        default: false
      },
      allowClear: {
        type: Boolean,
        default: true
      },
      // 选择后是否关闭 select
      closeOnSelect: {
        type: Boolean,
        default: false
      },
      maximumSelectionLength: {
        default: false
      },
      uploadFile: {
        type: [Object, Array]
      },
      selectWidth: {
        type: Number,
        default: 206
      }
    },
    template: '#vueselect2',
    data: function() {
      return {
        eventTicket: '',
        backspaceTimer: null,
        backspaceIndex: 0,
        touched: false,
        search: '',
        panelShow: false,
        styleObj: {
          width: '',
          inputWidth: ''
        },
        resultStyleObj: {
          top: ''
        },
        results: null,
        defer: 500,
        ajaxTimer: null,
        touched: false,
        loading: false //是否在读取数据
      };
    },
    computed: {},
    created: function() {
      // 全局点击
      // this.eventTicket = eventProxy.add(document,'click',this.foff,this);
      // document.addEventListener('click', this.foff.bind(this));
      // 初始化宽度
      if (!this.selectedList || this.selectedList.length === 0) {
        this.selectedList = [];
        this.styleObj.inputWidth = this.styleObj.width;
      } else {
        this.styleObj.inputWidth = '1em';
      }
    },
    ready: function() {
      if (this.selectWidth) {
        this.styleObj.width = this.selectWidth + 'px';
      } else {
        this.styleObj.width = this.$el.parentNode.clientWidth + 'px';
      }
      this.calcContainerHeight();
    },
    methods: {
      // 文档点击事件
      foff: function(e) {
        this.panelShow = false;
      },
      // 使 input获得焦点
      multiInputFocus: function() {
        // if(this.disabled){
        //   return false;
        // }
        try {
          this.$els.multiInput.focus();
        } catch (e) {
          this.$el.querySelector('#multiInput').focus();
        }
      },
      singleInputFocus: function() {
        if (this.disabled) {
          return false;
        }
        this.panelShow = true;
        this.$nextTick(function() {
          try {
            this.$els.singleInput.focus();
          } catch (e) {
            this.$el.querySelector('#singleInput').focus();
          }
        });
      },
      // 从选中中移除
      multiRemoveItem: function(item, index, event) {
        var result = this.selectedList.splice(index, 1).pop();
        if (result) result._selected = false;
        this.calcContainerHeight();
        this.multiCalcInputWidth();
      },
      // 键盘backspace 事件
      multiRemoveLastItem: function(e) {
        // 搜索内容为空的可以进入
        if (this.search === '') {
          // 清 timer
          clearTimeout(this.backspaceTimer);
          // 索引加1
          this.backspaceIndex++;
          // 创建 timer
          this.backspaceTimer = setTimeout(function() {
            // 过期了将索引重置
            this.backspaceIndex = 0;
          }.bind(this), this.defer);
        }
        // 输入框内容为空 且数组长度不为0,且500毫秒内双击 backspace可以删除选中内容
        if (this.search === '' && this.selectedList.length !== 0 && this.backspaceIndex === 2) {
          var result = this.selectedList.pop();
          result._selected = false;
          clearTimeout(this.backspaceTimer);
          this.backspaceIndex = 0;
          this.calcContainerHeight();
          this.multiCalcInputWidth();
        }
      },
      // 从结果中反选
      // deselectFromResults: function(id) {
      //   _.forEach(this.results, function(element, index, array) {
      //     if (id === element.id) {
      //       element._selected = false;
      //       return false;
      //     }
      //   });
      // },
      // 多选选中事件
      multiChoose: function(result, index, event) {
        // 已经选中过不做处理
        if (this.multiHasSelected(result.id)) {
          event.stopPropagation();
          return false;
        } else {
          // 加入数组
          // 确认选中
          result._selected = true;
          this.selectedList.push(result);
          this.search = '';
          this.multiCalcInputWidth();
          this.calcContainerHeight();
        }
      },
      // 多选键盘输入事件
      multiInputKeyup: function(event) {
        clearTimeout(this.ajaxTimer);
        this.ajaxTimer = setTimeout(function() {
          this.queryResult();
        }.bind(this), this.defer);
        this.multiCalcInputWidth();
        this.calcContainerHeight();
      },
      // 计算宽度
      multiCalcInputWidth: function() {
        // 选中内容为空占整行
        if (this.selectedList.length === 0) {
          this.styleObj.inputWidth = this.styleObj.width;
        } else {
          // 选中内容不为空按字数,每个占0.6em 宽度,刚开始会有跳跃感,无解
          var fontSize = parseInt(getComputedStyle(this.$els.multiInput).fontSize);
          this.styleObj.inputWidth = ((this.search.length + 2) * fontSize) + 'px';
          this.$nextTick(function() {
            // 渲染后宽度哦超过父容器的等于父容器宽度
            var inputWidth = parseInt(this.$els.multiInput.clientWidth);
            var parentWidth = parseInt(this.$el.parentNode.clientWidth);
            if (inputWidth > parentWidth) {
              this.styleObj.inputWidth = parentWidth + 'px';
            }
          });
        }
      },
      calcContainerHeight: function() {
        this.$nextTick(function() {
          this.$nextTick(function() {
            var height = Math.floor(parseFloat(getComputedStyle(this.$els.container).height));
            this.resultStyleObj.top = (height + 1) + 'px';
          });
        });
      },
      // 是否被选中
      multiHasSelected: function(id) {
        var result = false;
        _.forEach(this.selectedList, function(element, index, array) {
          result = (id === element.id);
          if (result) {
            return false;
          }
        });
        return result;
      },
      fakeItem: function() {
        if (this.fakeAble && this.search.trim() !== '') {
          this.selectedList.push({
            id: Date.now(),
            name: this.search
          });
          this.search = '';
          this.multiCalcInputWidth();
          this.calcContainerHeight();
        } else {
          return false;
        }
      },
      // 单选 清除选择项
      singleClearSelected: function(e) {
        var result = this.selectedList.pop();
        result._selected = false;
        this.selectedList = [];
        // this.deselectFromResults(result.id);
      },
      singleInputClick: function(e) {

      },
      // 单选输入
      singleInputKeyup: function() {
        clearTimeout(this.ajaxTimer);
        this.ajaxTimer = setTimeout(function() {
          this.queryResult();
        }.bind(this), this.defer);
      },
      singleChoose: function(result, index, event) {
        if (this.selectedList.length !== 0 && this.selectedList[0].id === result.id) {
          event.stopPropagation();
          return false;
        } else {
          // 处理上一个对象为未选中
          var lastResult = this.selectedList.pop();
          if (lastResult) {
            lastResult._selected = false;
          }

          result._selected = true;
          // 加入数组
          this.selectedList = [];
          this.selectedList.push(result);
          // 确认选中
        }
      },
      // 获得焦点
      inputFocus: function(e) {
        if (this.disabled) {
          return false;
        }
        this.panelShow = true;
        this.queryResult();
        if (!this.touched) this.$dispatch('select2-touched');
      },
      queryResult: function() {
        var self = this;
        self.loading = true;
        var xmlHttp = this.$http.get(ctx + this.url, {
          params: {
            name: self.search
          }
        }).then(function(res) {
          var list = res.data.data.rows || res.data.data.list;
          // 没内容
          if (!list || list.length === 0) {
            self.results = null;
            return false;
          }
          // 有内容
          var results = [];
           // 判断是否为权限转移接收人
          // if(this.url.indexOf('activityOpt/userlist') >= 0){
          //   _.forEach(list, function(element, index, array) {
          //     results.push({
          //       id: element.memberid,
          //       name: element.name
          //     });
          //   });
          // }else{
            _.forEach(list, function(element, index, array) {
              results.push({
                id: element.id,
                name: element.name,
                accountName:element.phone,
                orgName:element.pinyinInitial,
                gradeName:element.address,
                _selected: self.isSelected(element.id)
              });
            // });
          });
          self.results = results;
        }, function(res) {
          self.results = null;
        }).catch(function(err) {
          self.results = null;
        }).finally(function() {
          // setTimeout(function() {
          self.loading = false;
          // }, 10000);
        });
      },
      isSelected: function(id) {
        var self = this;
        var result = false;
        _.forEach(self.selectedList, function(element, index, array) {
          if (element.id == id) {
            result = true;
            return false;
          }
        });
        return result;
      }
    },
    watch: {
      'panelShow': function(newVal, oldVal) {
        var self = this;
        if (newVal) {
          this.eventTicket = eventProxy.add(document, 'click', this.foff, this);
        } else {
          this.search = '';
          eventProxy.remove(document, 'click', this.eventTicket);
        }
      }
    },
    beforeDestroy: function() {
      eventProxy.remove(document, 'click', this.eventTicket);
    }
  });
  Vue.component('vue-select2', VueSelect2);
})();
