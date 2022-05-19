<template>
  <div>
    <el-menu
        style="width: 200px;height: calc(100vh - 50px)"
        default-active="2"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
    >
      <el-menu-item index="1">
        <template #title>
          <el-icon><location /></el-icon>
          <span>学生信息</span>
        </template>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><setting /></el-icon>
        <span>课程信息</span>
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