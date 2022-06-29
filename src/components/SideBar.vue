<template>
  <div>
    <el-menu
        style="width: 200px;height: calc(100vh - 50px)"
        default-active="/admin/student"
        router
        class="el-menu-vertical-demo"
        background-color="#2c3e50"
        text-color="#fff"
        active-text-color="#ffd04b"

    >
      <el-menu-item index="/admin/car">
        <template #title>
          <el-icon>
            <User/>
          </el-icon>
          <span>车辆管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/admin/fee">
        <el-icon>
          <EditPen/>
        </el-icon>
        <span>收费管理</span>
      </el-menu-item>
      <el-menu-item index="/admin/history">
        <el-icon>
          <EditPen/>
        </el-icon>
        <span>车辆出入记录</span>
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