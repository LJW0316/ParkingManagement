<template>
  <div>
    <!--    头部-->
    <HeadBar/>
    <!--    主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <SideBar/>
      <!--      内容区-->
      <AdminMain/>
      <router-view style="flex: 1"/>
    </div>
  </div>
</template>

<script>
import HeadBar from "@/components/Header";
import SideBar from "@/components/SideBar";
import AdminStudent from "@/views/Admin/Admin";

export default {
  name: "AdminLayout",
  components: {
    HeadBar,
    SideBar,
    // eslint-disable-next-line vue/no-unused-components
    AdminStudent
  }
}
</script>

<style scoped>

</style>
