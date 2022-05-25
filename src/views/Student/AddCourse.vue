<template>
  <el-table :data="state.tableData" border style="width: 100%">
    <el-table-column prop="cno" label="课号" width="180" sortable/>
    <el-table-column prop="cname" label="课程名称" width="180"/>
    <el-table-column prop="credit" label="学分" width="180"/>
    <el-table-column prop="cdept" label="学院" width="180"/>
    <el-table-column prop="tno" label="教师号" width="180"/>
    <el-table-column label="Operations">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.row.cno,scope.row.tno)"
        >选课</el-button
        >
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import {onMounted, reactive} from "vue";
import {localGet} from "@/utils";
import axios from "@/utils/axios";
import {ElMessage} from "element-plus";

export default {
  name: "AddCourse",
  setup() {
    const state = reactive({
      loading: false,
      tableData: [], // 数据列表
    })
    const sno = localGet('token').id
    onMounted(() => {
      getCourses()
    })
    const getCourses = () => {
      state.loading = true
      axios.get('student/coursesAvail', {
        params: {
          sno: sno
        }
      }).then(res => {
        state.tableData = res
        state.loading = false
      })
    }
    const handleEdit =(cno,tno)=>{
      axios.post('/student/addCourse', {
        cno:cno,
        tno:tno,
        sno:sno
      }).then(res => {
        ElMessage.success("选课成功")
        getCourses()
      })
    }
    return {
      state,
      getCourses,
      handleEdit
    }
  }
}
</script>

<style scoped>

</style>