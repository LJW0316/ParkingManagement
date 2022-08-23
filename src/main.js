import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import VueAxios from "vue-axios";
// import '@/assets/css/global.css'

const app = createApp(App)
app.use(ElementPlus, {
    locale: zhCn,
})
app.use(ElementPlus)
app.use(router)
app.use(VueAxios, axios)
app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
//路由拦截，防止未登录用户进入主界面
router.beforeEach((to, from, next) => {
    if (to.meta.requireAuth) {
        let userJson = sessionStorage.getItem("user");
        if (userJson) {
            next();
        } else {
            next({
                path: '/',
                query: {redirect: to.fullPath}
            })
        }
    } else {
        next();
    }
})