<template>
  <div style="width: 100%;height: 100vh;background-color: saddlebrown;overflow: hidden">
    <div style="width: 400px; margin:100px auto">
      <div style="color: #cccccc;font-size: 30px;text-align: center;padding:30px 0">欢迎登录</div>
      <el-form :model="form" size="normal">
        <el-form-item>
          <el-input prefix-icon="Avatar" v-model="form.username"/>
        </el-form-item>
        <el-form-item>
          <el-input prefix-icon="Lock" v-model="form.password" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from '@/utils/axios'
import {ElMessage} from 'element-plus'
import md5 from 'js-md5'

export default {
  name: "LoginPage",
  data() {
    return {
      form: {}
    }
  },
  methods: {
    login() {
      this.form.password = md5(this.form.password)//md5加密
      console.log(this.form)
      axios.post("/user/login",this.form).then(res => {
        console.log(res)
        if (res === this.form.username) {
          ElMessage.success("登录成功")
          this.$router.push("/admin")//登陆成功之后页面跳转到主页
        }
        else {
          ElMessage.error("登录失败")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>