<template>
  <el-menu
      class="function"
      router
      @select="handleSelect"
      text-color="#222"
      active-text-color="red">
    <el-menu-item index="/teacher/grade">
      <el-icon><EditPen /></el-icon>
      <span>成绩录入</span>
    </el-menu-item>
    <el-menu-item index="课表查询">
      <i class="el-icon-menu"></i>
      <span>课表查询</span>
    </el-menu-item>
    <el-menu-item index="/teacher/analysis">
      <el-icon><Histogram /></el-icon>
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