<template>
  <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      style="height: 50px;line-height: 50px;border-bottom: 1px solid #ffffff;display: flex"
  >
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: #ffd04b">教务管理系统</div>
    <div style="flex: 1px"></div>
    <div style="width: 100px">
      <el-dropdown>
        <el-button type="primary">
          下拉菜单
          <el-icon class="el-icon--right">
            <arrow-down/>
          </el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "HeadBar"
}
</script>

<style scoped>

</style>