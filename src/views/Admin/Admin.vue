<template>
  <div style="padding: 10px">
    <!--      功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入车牌号" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    表格区域-->
    <el-table :data="tableData" border stripe style="table-layout:auto;width: 100%">
      <el-table-column prop="no" label="车牌号" sortable/>
      <el-table-column prop="in_time" label="入库时间" sortable/>
      <el-table-column prop="out_time" label="出库时间" sortable/>
      <el-table-column prop="time" label="时长" sortable/>
      <el-table-column prop="type" label="车牌类型"/>
      <el-table-column prop="fee" label="计费" sortable/>
      <el-table-column prop="password" label="状态"/>
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button text size="small" @click="handleEdit(scope.row)" type="primary">编辑</el-button>
          <el-popconfirm title="确认删除吗？" @confirm="handleDelete(scope.row.sno)">
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
          v-model:currentPage="currentPage"
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
          <el-form-item label="车牌号">
            <el-input v-model="form.no" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="车牌类型">
            <el-input v-model="form.type" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="入库时间">
            <el-input v-model="form.in_time" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="出库时间">
            <el-input v-model="form.out_time" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="时长">
            <el-input v-model="form.time" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="计费">
            <el-input v-model="form.fee" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="状态">
            <el-radio v-model="form.status" label="出库">出库</el-radio>
            <el-radio v-model="form.status" label="入库">入库</el-radio>
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

var add_bool = false//用于判断新增

export default {
  name: "AdminStudent",
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
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
          axios.get('/student', {
            params: {
              pageNum: this.currentPage,
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
          add_bool = true
        },
        save() {
          console.log(add_bool)
          if (add_bool == true)//新增
          {
            axios.post('/student', this.form).then(res => {
              console.log(res)
              ElMessage.success("新增成功")
              this.load()//刷新表格数据
              this.dialogVisible = false //关闭弹窗
              add_bool = false//新增判断为false
            })
          } else//更新
          {
            axios.put('/student', this.form).then(res => {
              console.log(res)
              ElMessage.success("更新成功")
              this.load()//刷新表格数据
              this.dialogVisible = false //关闭弹窗
            })
          }
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
        handleDelete(sno) {
          console.log(sno)
          axios.delete('/student/' + sno).then(res => {
            ElMessage.success("删除成功")
            this.load()//刷新表格数据
          })
        }
      }
}
</script>

<style scoped>

</style>
