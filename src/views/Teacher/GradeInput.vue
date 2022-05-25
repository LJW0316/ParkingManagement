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
      </template>
    </el-table-column>
  </el-table>
  <div>
    <el-pagination
        v-model:currentPage="state.currentPage"
        v-model:page-size="state.pageSize"
        :page-sizes="[2, 5, 10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="state.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
  <EditGrade ref='editGrade' :reload="getClasses"/>
</template>


<script>
import axios from '@/utils/axios'
import {onMounted, reactive, ref} from "vue";
import {localGet} from "@/utils";
import EditGrade from "@/views/Teacher/EditGrade";
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
    const tno=localGet('token').id
    // mounted
    onMounted(() => {
      getGrades()
    })
    const getGrades = () => {
      state.loading=true
      axios.get('teacher/course',{
        params: {
          pageNum: state.currentPage,
          pageSize: state.pageSize,
          tno: tno
        }
      }).then(res => {
        state.tableData = res.records
        state.total = res.total
        state.loading = false
      })
    }
    const handleEdit = (cno,sno) => {
      editGrade.value.open(cno,sno)
    }
    const handleSizeChange = () => {//改变每页个数
      getGrades()
    }
    const handleCurrentChange = () => {//改变当前页码
      getGrades()
    }
    return {
      state,
      handleEdit,
      editGrade,
      getGrades,
      handleSizeChange,
      handleCurrentChange
    }
  }
}
</script>

<style scoped>

</style>