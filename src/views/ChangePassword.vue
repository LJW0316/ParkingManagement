<template>
  <el-dialog
      title="修改密码"
      v-model="visible"
      width="400px"
      @close="handleClose"
  >
    <el-form :model="ruleForm" :rules="rules" ref="formRef" label-width="100px" class="good-form">
      <el-form-item label="密码" prop="password">
        <el-input type="text" v-model="ruleForm.password"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import md5 from 'js-md5'
import { reactive, ref, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
import {localGet} from "@/utils";
export default {
  name: "ChangePassword",
  props: {
    reload: Function
  },
  setup() {
    const formRef = ref(null)
    const route = useRoute()
    const state = reactive({
      visible: false,
      ruleForm: {
        password: ''
      },
      rules: {
        grade: [
          { required: 'true', message: '成绩不能为空', trigger: ['change'] }
        ]
      }
    })
    const role = localGet('token').role
    const id = localGet('token').id
    // 开启弹窗
    const open = () => {
      state.visible = true
    }
    // 关闭弹窗
    const close = () => {
      state.visible = false
    }
    const handleClose = () => {
      formRef.value.resetFields()
    }
    const submitForm = () => {
      formRef.value.validate((valid) => {
        if (valid) {
          if(role === "学生")
            axios.put("/student",{
              sno:id,
              password:md5(state.ruleForm.password)
            }).then(res=>{
              ElMessage.success("成功更新")
              state.visible = false
            })
          else
            axios.put("/teacher",{
              tno:id,
              password:md5(state.ruleForm.password)
            }).then(res=>{
              ElMessage.success("成功更新")
              state.visible = false
            })
        }
      })
    }
    return {
      ...toRefs(state),
      open,
      close,
      formRef,
      submitForm,
      handleClose
    }
  }
}
</script>