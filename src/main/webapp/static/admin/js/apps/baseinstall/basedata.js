Vue.validator('numeric', function (val) {
  return /^[-+]?[0-9]+$/.test(val)
})

Vue.filter('haveElevator-filter', function(val) {
    switch(val) {
        case 0: 
            return '无'
        case 1:
            return '有'
    }
})

Vue.filter('feeType-filter', function(val) {
    switch(val) {
        case 'ADD': 
            return '增项'
        case 'REDUCE':
            return '减项'
        case 'COMPREHENSIVE':
            return '基装综合费'
    }
})

Vue.filter('feeType-filter2', function(val) {
    switch(val) {
        case 'ADD':
            return '增项'
        case 'REDUCE':
            return '减项'
        case 'COMPREHENSIVE':
            return '其他综合费'
    }
})

Vue.filter('dateFormater-filter', function(val) {
    return moment(val).format('YYYY-MM-DD');
})

+(function () {
	$('#baseInstall').addClass('active');
  $('#measureUnit').addClass('active');
  var vueRole = new Vue({
    el: '#container',
    mixins: [DaMeiVueMixins.DataTableMixin],
    data: {
      breadcrumbs: [{
        path: '/',
        name: '主页'
      },
        {
          path: '/',
          name: '基装数据',
          active: true
        }],
        id: '',
        baseMain: null
    },
    created: function () {
        this.id = DaMeiUtils.parseQueryString().id || ''
        this.fetchDetail()
    },
    ready: function () {
        var self = this;
        
    },
    methods: {
      fetchDetail: function() {
        var self = this;
        self.$http.get('/api/customerContract/' + self.id)
            .then(function(res) {
                console.log(res.data)
                if (res.data.code == 1) {
                    self.baseMain = res.data.data
                }
            })
      }
    }
  });

})();