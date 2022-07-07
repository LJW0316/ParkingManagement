<template>
  <div style="padding: 10px">
    <!--      功能区域-->
    <!--    <div style="margin: 10px 0">-->
    <!--      <el-button type="primary" @click="add">新增</el-button>-->
    <!--    </div>-->
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入车牌号" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    表格区域-->
    <el-table :data="tableData" border stripe style="table-layout:auto;width: 100%">
      <el-table-column prop="carNum" label="车牌号"/>
      <el-table-column prop="inTime" label="入库时间"/>
      <el-table-column prop="outTime" label="出库时间"/>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button text size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <!--    分页-->
      <el-pagination
          v-model:page-Num="pageNum"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :small="small"
          :disabled="disabled"
          :background="background"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
      <!--弹窗功能-->
      <el-dialog
          v-model="dialogVisible"
          title="编辑信息"
          width="30%"
      >
        <el-form :model="form" label-width="120px">
          <el-form-item label="ID">
            <el-input v-model="form.id" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="车牌号">
            <el-input v-model="form.carNum" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="入库时间">
            <el-input v-model="form.inTime" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="出库时间">
            <el-input v-model="form.outTime" style="width: 80%"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="save">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import axios from '@/utils/axios'
import {ElMessage} from 'element-plus'

export default {
  name: "AdminCar",
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      pageNum: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
    }
  },
  created() {
    this.load()
  },
  methods:
      {
        load() {
          axios.get('/admin/get_cars', {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              search: this.search
            }
          }).then(res => {
            console.log(res)
            this.tableData = res.records
            this.total = res.total
          })
        },
        add() {
          this.dialogVisible = true
          this.form = {}          // 清空表单操作
        },
        save() {
          axios.put('/student', this.form).then(res => {
            console.log(res)
            ElMessage.success("更新成功")
            this.load()//刷新表格数据
            this.dialogVisible = false //关闭弹窗
          })
        },
        handleEdit(row) {
          this.form = JSON.parse(JSON.stringify(row))
          this.dialogVisible = true
        },
        handleSizeChange() {//改变每页个数
          this.load()
        },
        handleCurrentChange() {//改变当前页码
          this.load()
        },
        handleDelete(id) {
          console.log(id)
          axios.delete('/admin/delete_car/' + id).then(res => {
            ElMessage.success("删除成功")
            this.load()//刷新表格数据
          })
        }
      }
}
</script>

<style scoped>

</style>
