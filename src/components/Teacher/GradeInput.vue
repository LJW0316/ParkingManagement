<template>
  <el-table :data="state.tableData" style="width: 100%">
    <el-table-column label="课程号" width="180">
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span style="margin-left: 10px">{{ scope.row.cno }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="学号" width="180">
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span style="margin-left: 10px">{{ scope.row.sno }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="成绩" width="180">
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span style="margin-left: 10px">{{ scope.row.grade }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="绩点" width="180">
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span style="margin-left: 10px">{{ scope.row.point }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column label="Operations">
      <template #default="scope">
        <el-button size="small" @click="handleEdit(scope.row.id)"
        >编辑</el-button
        >
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
        >Delete</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <EditGrade ref='editGrade' :reload="getClasses"/>
</template>


<script>
import axios from '@/utils/axios'
import {onMounted, reactive, ref} from "vue";
import {localGet} from "@/utils";
import EditGrade from "@/components/Teacher/EditGrade";
export default {
  name: "GradeInput",
  components:{
    EditGrade
  },
  setup(){
    const editGrade =ref(null)
    const state = reactive({
      loading: false,
      tableData: [], // 数据列表
      multipleSelection: [], // 选中项
      total: 0, // 总条数
      currentPage: 1, // 当前页
      pageSize: 10 // 分页大小
    })
    const tno=localGet('id')
    // mounted
    onMounted(() => {
      getClasses()
    })
    const getClasses = () => {
      state.loading=true
      console.log(localGet('id'))
      axios.post('teacher/course',{
        tno:tno
      }).then(res => {
        state.tableData = res
        state.loading = false
      })
    }
    const handleEdit = (cno,sno) => {
      editGrade.value.open(cno,sno)
    }
    return {
      state,
      handleEdit,
      editGrade,
      getClasses
    }
  }
}
</script>

<style scoped>

</style>