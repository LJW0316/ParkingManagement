<template>
  <div id='building'>
    <div class="login-body">
      <div class="login-container">
        <div class="head">
          <div class="name">
            <div class="title">教务管理系统</div>
            <div class="tips">用户登录页面</div>
          </div>
        </div>
        <el-form label-position="top" :model="ruleForm" status-icon :rules="rules" ref="loginForm" class="login-Form">
          <el-form-item label="账号" prop="username">
            <el-input
                v-model="ruleForm.username"
                placeholder="请输入账户"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
                v-model="ruleForm.password"
                type="password"
                placeholder="请输入密码"
                show-password
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/utils/axios'
import { reactive, ref, toRefs } from 'vue'
import md5 from 'js-md5'
export default {
  name:'LoginPage',
  setup() {
    const loginForm = ref(null)
    const state = reactive({
      ruleForm: {
        username: '',
        password: ''
      },
      checked: true,
      rules: {
        username: [
          { required: 'true', message: '账户不能为空', trigger: 'blur' }
        ],
        password: [
          { required: 'true', message: '密码不能为空', trigger: 'blur' }
        ]
      }
    })
    const submitForm = async () => {
      loginForm.value.validate((valid) => {
        if (valid) {
          axios.post('/adminUser/login', {
            userName: state.ruleForm.username || '',
            passwordMd5: md5(state.ruleForm.password)
          }).then(res => {
            if(res.data.Data == 'student')
              window.location.href = '/student';
            else if(res.data.Data == 'teacher')
              window.location.href = '/teacher';
            else if(res.data.Data == 'administrator')
              window.location.href = '/administrator';
          })
        } else {
          console.log('submit error!!')
          return false;
        }
      })
    }
    const resetForm = () => {
      loginForm.value.resetFields();
    }
    return {
      ...toRefs(state),
      loginForm,
      submitForm,
      resetForm,
    }
  }
}
</script>

<style scoped>
#building{
  background:url("../assets/shu.jpg");
  width:100%;
  height:100%;
  position:fixed;
  background-size:100% 100%;
}
.login-body {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}
.login-container {
  width: 420px;
  height: 500px;
  background-color:rgba(211,211,211,0.7);
  border-radius: 4px;
  margin-top: 40px;
  box-shadow: 0 21px 41px 0 rgba(0, 0, 0, 0.2);
}
.head {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0 20px 0;
}
.login-Form {
  width: 70%;
  margin: 0 auto;
}
</style>
