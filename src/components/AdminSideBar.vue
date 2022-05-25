<template>
  <div>
    <el-menu
        style="width: 200px;height: calc(100vh - 50px)"
        default-active="/admin/student"
        router
        class="el-menu-vertical-demo"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"

    >
      <el-menu-item index="/admin/student">
        <template #title>
          <el-icon>
            <User/>
          </el-icon>
          <span>学生管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/admin/teacher">
        <el-icon>
          <EditPen/>
        </el-icon>
        <span>教师管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/course">
        <el-icon>
          <Tickets/>
        </el-icon>
        <span>课程管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/studentcourse">
        <el-icon>
          <Tickets/>
        </el-icon>
        <span>学生成绩管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "SideBar"
}
</script>

<style scoped>

</style>