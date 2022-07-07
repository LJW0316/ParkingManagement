<template>
  <div style="padding: 10px">
    <!--      功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">修改车位数量</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入车牌号" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    表格区域-->
    <el-table :data="tableData" border stripe style="table-layout:auto;width: 100%">
      <el-table-column prop="plate" label="车牌号"/>
      <el-table-column prop="orderNo" label="订单号"/>
      <el-table-column prop="totalFee" label="订单价格"/>
      <el-table-column prop="orderStatus" label="订单状态"/>
      <el-table-column prop="createTime" label="创建时间"/>
      <el-table-column prop="updateTime" label="更新时间"/>
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
          <el-form-item label="总车位">
            <el-input v-model="form.newNum" style="width: 80%"/>
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
  name: "AdminOrder",
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
          axios.get('/admin/get_orders', {
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
          axios.put('/parking/alter_num', {
            newNum:this.form.newNum
          }).then(res => {
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
