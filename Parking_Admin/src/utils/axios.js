import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router/index'
import { localGet } from './index'

// 这边由于后端没有区分测试和正式，姑且都写成一个接口。
axios.defaults.baseURL = 'http://47.103.85.74:8090/api'
//axios.defaults.baseURL = 'http://localhost:8090/api'
// 携带 cookie，对目前的项目没有什么作用，因为我们是 token 鉴权
//axios.defaults.withCredentials = true
// 请求头，headers 信息
axios.defaults.headers['X-Requested-With'] = 'XMLHttpRequest'
axios.defaults.headers['token'] = localGet('token') || ''
// 默认 post 请求，使用 application/json 形式
axios.defaults.headers.post['Content-Type'] = 'application/json'
axios.defaults.headers.put['Content-Type'] = 'application/x-www-form-urlencoded'
// 请求拦截器，内部根据返回值，重新组装，统一管理。
axios.interceptors.response.use(res => {
    if (res.data.code != 200) {
        if (res.data.msg) ElMessage.error(res.data.msg)
        if (res.data.code === 419) {
            router.push({ path: '/' })
        }
        return Promise.reject(res.data)
    }
    return res.data.data
})

export default axios