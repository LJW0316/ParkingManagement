<template>
  <el-menu
      class="function"
      default-active="教师首页"
      router
      @select="handleSelect"
      text-color="#222"
      active-text-color="red">
    <el-menu-item index="admin">
      <i class="el-icon-menu"></i>
      <span>教师首页</span>
    </el-menu-item>
    <el-menu-item index="成绩录入">
      <i class="el-icon-menu"></i>
      <span>成绩录入</span>
    </el-menu-item>
    <el-menu-item index="课表查询">
      <i class="el-icon-menu"></i>
      <span>课表查询</span>
    </el-menu-item>
    <el-menu-item index="成绩分析">
      <i class="el-icon-menu"></i>
      <span>成绩分析</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "TeacherSideMenu"
}
</script>

<style scoped>
.function {
  position: fixed;
  margin-left: 50%;
  left: -600px;
  top: 100px;
  width: 150px;
}
</style>