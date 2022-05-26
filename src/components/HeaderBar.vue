<template>
  <div
      style="background-color: #2c3e50;
      height: 50px;line-height: 50px;
      border-bottom: 1px solid #ccc;
      display: flex"
  >
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: whitesmoke">教务管理系统</div>
    <div style="flex: 1px"></div>
    <div style="width: 100px">
      <div style="width: 100px">
        <el-dropdown>
          <el-button type="primary" text>
            {{ name }}
            <el-icon class="el-icon--right">
              <arrow-down/>
            </el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="openDialog">个人信息</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
  <ChangePassword ref='changePassword'></ChangePassword>
</template>

<script>
import {localGet,localClear} from "@/utils";
import router from "@/router";
import ChangePassword from "@/views/ChangePassword"
import {ref} from "vue";
export default {
  name: "HeadBar",
  components: {ChangePassword},
  setup(){
    const changePassword=ref(null)
    const name=localGet('token').name
    const openDialog =()=>{
      changePassword.value.open()
    }
    const logout=()=>{
      localClear()
      router.push('/')
    }
    return{
      name,
      logout,
      changePassword,
      openDialog
    }
  }
}
</script>

<style scoped>

</style>