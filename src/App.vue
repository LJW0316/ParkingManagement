<template>
  <div>
<!--    头部-->
    <HeadBar />
<!--    主题-->
    <div style="display: flex">
<!--      侧边栏-->
      <SideBar />
<!--      内容区域-->
      <router-view style="flex: 1"/>
    </div>
  </div>

</template>

<script>
import HeadBar from "@/components/HeadBar";
import SideBar from "@/components/SideBar";

export default {
  name: 'App',
  components: {
    HeadBar,
    SideBar
  }
}
</script>

<style>

</style>
