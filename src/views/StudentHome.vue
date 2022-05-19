<template>
  <div style="padding: 10px">
    <div style="margin: 10px 0">
<!--      功能区域-->
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%"/>
      <el-button type="primary" style="margin-left: 5px">查询</el-button>
    </div>
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="sno" label="学号" sortable/>
      <el-table-column prop="name" label="姓名"/>
      <el-table-column prop="age" label="年龄"/>
      <el-table-column prop="sex" label="性别"/>
      <el-table-column prop="sdept" label="学院"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column fixed="right" label="Operations">
        <template #default="scope">
          <el-button text size="small" @click="handleEdit(scope.row)" type="primary">编辑</el-button>
          <el-popconfirm title="确认删除吗？">
            <template #reference>
              <el-button text size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
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
          title="新增"
          width="30%"
      >
        <el-form :model="form" label-width="120px">
          <el-form-item label="学号">
            <el-input v-model="form.sno" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="年龄">
            <el-input v-model="form.age" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio v-model="form.sex" label="男" >男</el-radio>
            <el-radio v-model="form.sex" label="女" >女</el-radio>
          </el-form-item>
          <el-form-item label="学院">
            <el-input v-model="form.sdept" style="width: 80%"/>
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save"
        >确认</el-button
        >
      </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "StudentHomePage",
  components: {},
  data()
  {
    return {
      form:{},
      dialogVisible:false,
      search:'',
      currentPage:1,
      pageSize:10,
      total:10,
      tableData: [

      ]
    }
  },
  methods:
      {
        add(){
          this.dialogVisible = true
          this.form = {}          // 清空表单操作
        },
        save(){
          axios.post('/student',this.form).then(res => {
            console.log(res)
          })
        },
        handleEdit()
        {

        },
        handleSizeChange()
        {

        },
        handleCurrentChange()
        {

        }
      }
}
</script>

<style scoped>

</style>
