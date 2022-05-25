<template>
  <el-menu
      class="function"
      router
      @select="handleSelect"
      text-color="#222"
      active-text-color="red">
    <el-menu-item index="/student/addCourse">
      <el-icon><DocumentAdd /></el-icon>
      <span>选课</span>
    </el-menu-item>
    <el-menu-item index="/student/lookUpCourse">
      <i class="el-icon-menu"></i>
      <span>课表查询</span>
    </el-menu-item>
    <el-menu-item index="/student/lookUpGrade">
      <i class="el-icon-menu"></i>
      <span>成绩查询</span>
    </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: "StudentSideMenu"
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