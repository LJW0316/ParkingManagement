<template>
  <el-table :data="state.tableData" border style="width: 100%">
    <el-table-column prop="cno" label="课号" width="180" />
    <el-table-column prop="cname" label="课程名称" width="180" />
    <el-table-column prop="grade" label="成绩" width="180"/>
    <el-table-column prop="point" label="绩点" />
  </el-table>
</template>

<script>
import {onMounted, reactive} from "vue";
import {localGet} from "@/utils";
import axios from "@/utils/axios";

export default {
  name: "LookupGrade",
  setup(){
    const state = reactive({
      loading: false,
      tableData: [], // 数据列表
    })
    const sno=localGet('token').id
    onMounted(() => {
      getGrades()
    })
    const getGrades = () => {
      state.loading=true
      axios.get('student/grades',{
        params: {
          sno: sno
        }
      }).then(res => {
        state.tableData = res
        state.loading = false
      })
    }
    return {
      state,
      getGrades
    }
  }
}
</script>

<style scoped>

</style>