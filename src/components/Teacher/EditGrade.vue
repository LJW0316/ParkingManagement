<template>
  <el-dialog
      title="登分"
      v-model="visible"
      width="400px"
      @close="handleClose"
  >
    <el-form :model="ruleForm" :rules="rules" ref="formRef" label-width="100px" class="good-form">
      <el-form-item label="课号" prop="name">
        <el-input type="text" disabled v-model="cno"></el-input>
      </el-form-item>
      <el-form-item label="学号" prop="name">
        <el-input type="text" disabled v-model="sno"></el-input>
      </el-form-item>
      <el-form-item label="成绩" prop="rank">
        <el-input type="number" max='200' v-model="ruleForm.grade"></el-input>
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
import { reactive, ref, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/utils/axios'
import { ElMessage } from 'element-plus'
export default {
  name: "EditGrade",
  props: {
    reload: Function
  },
  setup(props) {
    const formRef = ref(null)
    const route = useRoute()
    const state = reactive({
      visible: false,
      ruleForm: {
        grade: ''
      },
      rules: {
        grade: [
          { required: 'true', message: '成绩不能为空', trigger: ['change'] }
        ]
      },
      id: '',
      cno: '',
      sno: ''
    })
    // 获取详情
    const getDetail = (id) => {
      axios.get(`/teacher/getGrade/${id}`).then(res => {
        state.ruleForm = {
          grade: res.grade
        }
        state.cno = res.cno
        state.sno = res.sno
      })
    }
    // 开启弹窗
    const open = (id) => {
      state.visible = true
      if (id) {
        state.id = id
        getDetail(id)
      }
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
            axios.put('/teacher/grade', {
              id: state.id,
              grade: state.ruleForm.grade
            }).then(() => {
              ElMessage.success('修改成功')
              state.visible = false
              if (props.reload) props.reload()
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