<template>
  <div>
    <!--    头部-->
    <HeadBar/>
    <!--    主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <SideBar/>
      <!--      内容区-->
      <StudentMain/>
      <router-view style="flex: 1"/>
    </div>
  </div>
</template>

<script>
import HeadBar from "@/components/HeadBar";
import SideBar from "@/components/SideBar";
import StudentMain from "@/components/Admin/AdminHome";

export default {
  name: "StudentHomePage",
  components: {
    HeadBar,
    SideBar,
    // eslint-disable-next-line vue/no-unused-components
    StudentMain
  }
}
</script>

<style scoped>

</style>
