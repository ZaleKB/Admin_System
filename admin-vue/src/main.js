import Vue from 'vue'
//import VueI18n from 'vue-i18n'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"

import enLocale from 'element-ui/lib/locale/lang/en'

import axios from './axios'
import global from './globalFun'



Vue.use(ElementUI,{ enLocale })
Vue.prototype.$axios = axios //

Vue.config.productionTip = false

//require("./mock.js")    //用来mock数据 后端介入后不需要

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
