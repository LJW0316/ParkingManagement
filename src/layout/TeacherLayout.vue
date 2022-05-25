<template>
  <div class="common-layout">
    <el-container>
      <el-header class="nav-menu">
        <HeaderBar></HeaderBar>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <TeacherSideMenu></TeacherSideMenu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import TeacherSideMenu from "@/components/TeacherSideMenu";
import HeaderBar from "@/components/HeaderBar";
export default {
  name: "TeacherHomePage",
  // eslint-disable-next-line vue/no-unused-components
  components:{TeacherSideMenu,HeaderBar}
}
</script>

<style scoped>
.nav-menu {
  /*margin-bottom: 40px;*/
  box-shadow: 0 2px 4px 0 rgba(0,0,0,.05);
}
</style>