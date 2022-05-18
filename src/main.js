import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import VueAxios from "vue-axios";
import '@/assets/css/global.css'


const app = createApp(App)

app.use(ElementPlus, {
    locale: zhCn,
})
app.use(router)
app.use(VueAxios, axios)
app.mount('#app')
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}