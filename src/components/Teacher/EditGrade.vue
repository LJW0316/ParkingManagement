<template>
  <el-dialog
      title="登分"
      v-model="visible"
      width="400px"
      @close="handleClose"
  >
    <el-form :model="ruleForm" :rules="rules" ref="formRef" label-width="100px" class="good-form">
      <el-form-item label="课号" prop="name">
        <el-input type="text" v-model="ruleForm.cno"></el-input>
      </el-form-item>
      <el-form-item label="学号" prop="name">
        <el-input type="text" v-model="ruleForm.sno"></el-input>
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
        cno: '',
        sno: '',
        grade: ''
      },
      rules: {
        name: [
          { required: 'true', message: '名称不能为空', trigger: ['change'] }
        ],
        grade: [
          { required: 'true', message: '成绩不能为空', trigger: ['change'] }
        ]
      },

    })
    // 获取详情
    const getDetail = (id) => {
      axios.get(`/categories/${id}`).then(res => {
        state.ruleForm = {
          name: res.categoryName,
          rank: res.categoryRank
        }
        state.parentId = res.parentId
        state.categoryLevel = res.categoryLevel
      })
    }
    // 开启弹窗
    const open = (cno,sno) => {
      state.visible = true
      if (cno,sno) {
        state.ruleForm.cno = cno
        state.ruleForm.sno = sno
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
            axios.put('/categories', {
              categoryId: state.id,
              categoryLevel: state.categoryLevel,
              parentId: state.parentId,
              categoryName: state.ruleForm.name,
              categoryRank: state.ruleForm.rank
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